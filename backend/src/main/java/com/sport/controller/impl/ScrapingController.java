package com.sport.controller.impl;

import com.sport.controller.ScrapingApi;
import com.sport.entity.Address;
import com.sport.entity.Event;
import com.sport.entity.Region;
import com.sport.service.ScrapingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
public class ScrapingController implements ScrapingApi {

    @Autowired
    private ScrapingService scrapingService;

    public ResponseEntity<String> scrapDetails(@PathVariable("year") int year) {

        scrapingService.getAllByYear(year);

        return ResponseEntity.ok("Scraping has begun for year " + year + "...");

    }


    public ResponseEntity<List<Address>> getStadiumInTown(@PathVariable("regionPostalCode") int regionPostalCode) {
        List<Address> addresses = scrapingService.StadiumInTown(regionPostalCode);
        log.info("StadiumInTown : " + addresses.size());
        return ResponseEntity.status(HttpStatus.OK).body(addresses);
    }

    @Override
    public ResponseEntity<List<Event>> getEventInTown(@PathVariable("regionPostalCode") int regionPostalCode) {
        List<Event> events = scrapingService.getEventInTown(regionPostalCode);
        log.info("EventInTown : " + events.size());
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    public ResponseEntity<List<Region>> getRegionList() {
        List<Region> regionList = scrapingService.getRegionList();
        return ResponseEntity.status(HttpStatus.OK).body(regionList);
    }
}
