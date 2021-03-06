package com.sport.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sport.entity.Address;
import com.sport.entity.Event;
import com.sport.entity.Organizer;
import com.sport.entity.Region;
import com.sport.repository.*;
import com.sport.utils.JsoupUtils;
import com.sport.utils.OpenStreetMapUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;



@Slf4j
@Service
public class ScrapingService {

    public static final int OLDER_THAN_TWO_DAYS = 2;
    @Autowired
    OpenStreetMapUtils openStreetMapUtils;
    @Autowired
    JsoupUtils jsoupUtils;
    @Autowired
    FileService fileService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    RegionRepository regionRepository;

    @Autowired
    OrganizerRepository organizerRepository;

    @Autowired
    ScrapingRepository scrapingRepository;

//    @Async
    public void getAllByYear(int year) {

        log.info("Parsing is starting...");

        long startProcessing = System.currentTimeMillis();

        String tmpDirectory = System.getProperty("java.io.tmpdir");
        String separator = System.getProperty("file.separator");
        String path = tmpDirectory + separator + "data" + separator + year;
        log.info("[has Detail File To Be Retrieved] path {} ", path);
        File folder = new File(path);

        List<Event> all = Arrays.stream(folder.listFiles())
                                .filter(file -> isValidFile(year, file.getAbsolutePath()))
                                .map(file -> scrapEvents(file))
                                .flatMap(List::stream)
                                .collect(Collectors.toList());


        List<Event> allDistinctEvent = new ArrayList<>(all.stream()
                                                          .parallel()
                                                          .filter(e -> e.getCode() != null)
                                                          .filter(event -> event.getDateDeDebut().isAfter(LocalDate.now()))
                                                          .collect(Collectors.toMap(Event::getCode, p -> p, (p, q) -> p))
                                                          .values());

        eventRepository.saveAll(allDistinctEvent);

        long duration = System.currentTimeMillis() - startProcessing;
        log.info("Elapsed time : {} min {} s {} ms", duration / 1000 / 60, duration / 1000 % 60, duration % 1000);
        log.info("There are {} events", allDistinctEvent.size());
    }

    private boolean isValidFile(int year, String file) {
        return file.contains(year + "") && file.endsWith(".html");
    }

    private boolean hasDetailFileToBeRetrieved(int year, String department, String id) {

        String tmpDirectory = System.getProperty("java.io.tmpdir");
        String separator = System.getProperty("file.separator");
        String url = tmpDirectory + separator + "data" + separator + year + separator + department + separator + id + ".html";
        File test = new File(url);

        if (!test.exists()) {
            return true;
        }

        LocalDate now = LocalDate.now();
        LocalDate fileTime = Instant.ofEpochMilli(test.lastModified()).atZone(ZoneId.systemDefault()).toLocalDate();

        Period period = Period.between(fileTime, now);
        int diff = period.getDays();

        return diff >= OLDER_THAN_TWO_DAYS;

    }


    private List<Event> scrapEvents(File file) {

        int year = Integer.parseInt(file.getParentFile().getName());
        String department = file.getName().split(".html")[0];

        Document doc = jsoupUtils.getDocument(file);

        List<String> ids = doc.getElementsByAttribute("href")
                              .parallelStream()
                              .filter(element -> element.attr("href").contains("bddThrowCompet"))
                              .map(element -> element.attr("href").split("'")[1])
                              .collect(Collectors.toList());

        ids.parallelStream()
           .filter(id -> hasDetailFileToBeRetrieved(year, department, id))
           .forEach(id -> fileService.getById(year, department, id));

        log.info("[scrap Events] for department {} ", department);
        return ids.stream()
                  .map(id -> parseEvent(year, department, id))
                  .collect(Collectors.toList());

    }

    private Event parseEvent(int year, String department, String id) {

        String tmpDirectory = System.getProperty("java.io.tmpdir");
        String separator = System.getProperty("file.separator");

        String file = tmpDirectory + separator + "data" + separator + year + separator + department + separator + id + ".html";

        Document doc = jsoupUtils.getDocument(new File(file));
        if (doc == null) {
            log.error("Could not parse file {}", file);
            return null;
        }

        Map<String, String> collectMap = new HashMap<>();

        collectMap.put("fileId", id);
        collectMap.put("code", scrapingRepository.getCode(doc));

        collectMap.put("title", scrapingRepository.getTitle(doc));
        collectMap.put("town", scrapingRepository.getTown(doc));

        collectMap.put("league", scrapingRepository.getLeague(doc));
        collectMap.put("department", scrapingRepository.getDepartment(doc));

        // types
        String type = scrapingRepository.getType(doc);

        if (type != null) {
            collectMap.put("type", type.split(" / ")[0]);
            String[] subTypes = type.split(" / ")[1].split(";");
        }

        collectMap.put("level", scrapingRepository.getLevel(doc));

        collectMap.put("technicalAdvice", scrapingRepository.getTechnicalAdvice(doc));

        // handle addresses
        Map<String, Map<String, String>> addresses = new HashMap<>();
        addresses.put("stadiumAddress", scrapingRepository.getStadiumAddress(doc));
        addresses.put("organisationAddress", scrapingRepository.getOrganisationAddress(doc));

        //handle contacts
        Map<String, String> contacts = scrapingRepository.getContacts(doc);
        Map<String, String> staff = scrapingRepository.getStaff(doc);

        // parse tests (epreuves)
        Map<String, Map<String, String>> tests = new HashMap<>();
        tests = scrapingRepository.getTests(doc);


        final ObjectMapper mapper = new ObjectMapper(); // jackson's object mapper to change with orika or mapstruct
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Event event = mapper.convertValue(collectMap, Event.class);

        Map<String, String> stadiumAddress = scrapingRepository.getStadiumAddress(doc);
        if (stadiumAddress != null) {
            Address address = mapper.convertValue(stadiumAddress, Address.class);

            if (address.getName() != null && address.getTown() != null && address.getPostalCode() != null) {
                Address foundAddress = addressRepository.findByNameAndTownAndPostalCode(address.getName(), address.getTown(), address.getPostalCode());
                if (foundAddress == null) {

                    Map<String, Double> coordinates = openStreetMapUtils.getCoordinates(address.getAddress());
                    address.setLatitude(coordinates.get("lat"));
                    address.setLongitude(coordinates.get("lon"));

                    addressRepository.save(address);
                    event.setStadiumAddress(address);
                } else {
                    event.setStadiumAddress(foundAddress);
                }
            }
        }

        Map<String, String> organisationAddress = scrapingRepository.getOrganisationAddress(doc);
        if (organisationAddress != null) {
            Organizer organizer = mapper.convertValue(organisationAddress, Organizer.class);
            Organizer foundOrganizerContact = organizerRepository.findByOrganisationAndTownAndPostalCode(organizer.getOrganisation(), organizer.getTown(), organizer.getPostalCode());
            if (foundOrganizerContact == null) {
                organizerRepository.save(organizer);
                event.setOrganizerContact(organizer);
            } else {
                event.setOrganizerContact(foundOrganizerContact);
            }
        }

        event.setDateDeDebut(scrapingRepository.getBeginDate(doc));
        event.setDateDeFin(scrapingRepository.getEndDate(doc));

        return event;
    }


    public List<Address> StadiumInTown(Integer postalCode) {
        String code = postalCode.toString();
        return addressRepository.findByTypeAndPostalCodeStartsWith("STD", code.length() == 1 ? "0" + code : code);
    }


    public List<Region> getRegionList() {
        return regionRepository.findAllByOrderByTownAsc();
    }

    public List<Event> getEventInTown(Integer postalCode) {
        String code = postalCode.toString();
        List<Event> byDepartmentStartsWith = eventRepository.findByDepartmentStartsWith(code.length() == 1 ? "00" + code : code.length() == 2 ? "0" + code : code);
        byDepartmentStartsWith.sort((Event h1, Event h2) -> h1.getDateDeDebut().compareTo(h2.getDateDeDebut()));
//        return byDepartmentStartsWith.stream().filter(event -> event.getDateDeDebut().isAfter(LocalDate.now())).collect(Collectors.toList());
        return byDepartmentStartsWith ;
    }

    public Event getEventByCode(String eventCode) {
        return eventRepository.findByCode(eventCode);
    }
}
