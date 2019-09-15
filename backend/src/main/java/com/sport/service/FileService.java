package com.sport.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

@Slf4j
@Service
public class FileService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    DepartmentService departmentService;
    @Value("${bases.athle.uri.base}")
    private String host;
    @Value("${bases.athle.uri.list}")
    private String hostArgsByYearAndDepartment;
    @Value("${bases.athle.uri.detail}")
    private String hostArgsById;

//    @Async
    public void getAllByYear(int year) {
        log.info("[get All By Year] started" );
        departmentService.getAll()
                         .stream()
                         .filter(dep -> getWebPageAsString(year, dep) != null)
                         .forEach(dep -> writeWebPageOnLocalFile(year, dep, getWebPageAsString(year, dep)));
        log.info("[get All By Year] ended" );

    }

    public void getById(int year, String department, String id) {

        String lf = getWebPageAsString(id);

        if (lf == null) {
            return;
        }

        writeWebPageOnLocalFile(year, department, id, lf);

    }


    private String getWebPageAsString(String id) {
        String uri = host.concat(hostArgsById)
                         .replace("%ID%", id);

        return restTemplate.getForObject(uri, String.class);
    }

    private String getWebPageAsString(int year, String department) {

        String uri = host.concat(hostArgsByYearAndDepartment)
                         .replace("%YEAR%", year + "")
                         .replace("%DEPARTMENT%", department);

        return restTemplate.getForObject(uri, String.class);
    }

    private void writeWebPageOnLocalFile(int year, String department, String content) {

        String dir = System.getProperty("java.io.tmpdir");
        String separator = System.getProperty("file.separator");
        String fileStr = dir + separator + "data" + separator + year + separator + department + ".html";

        new File(fileStr).getParentFile().mkdirs();
        OutputStream os;

        try {
            os = new FileOutputStream(fileStr);
        } catch (Exception e) {
            log.error("Could not write the file {}/{}", year, department, e);
            return;
        }

        final PrintStream printStream = new PrintStream(os);
        printStream.println(content);
        printStream.close();
    }

    private void writeWebPageOnLocalFile(int year, String department, String id, String content) {

        String dir = System.getProperty("java.io.tmpdir");
        String separator = System.getProperty("file.separator");
        String fileStr = dir + separator  + "data" + separator + year + separator + department + separator + id + ".html";

        new File(fileStr).getParentFile().mkdirs();
        OutputStream os;

        try {
            os = new FileOutputStream(fileStr);
        } catch (Exception e) {
            log.error("Could not write the file {}", id, e);
            return;
        }

        final PrintStream printStream = new PrintStream(os);
        printStream.println(content);
        printStream.close();
    }

}
