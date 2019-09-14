package com.sport.controller.impl;

import com.sport.controller.FileApi;
import com.sport.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FileController implements FileApi {

    @Autowired
    private FileService fileService;

    public ResponseEntity<String> getFiles(@PathVariable("year") int year) {

        fileService.getAllByYear(year);

        return ResponseEntity.ok("Files are being retrieved for year " + year + "...");

    }

}
