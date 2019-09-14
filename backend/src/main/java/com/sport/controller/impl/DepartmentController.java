package com.sport.controller.impl;

import com.sport.controller.DepartmentApi;
import com.sport.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class DepartmentController implements DepartmentApi {

    @Autowired
    private DepartmentService departmentService;


    public ResponseEntity<List<String>> getDepartmentsCodes() {

        List<String> departments = departmentService.getAll();

        if (departments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(departments);
        }

        return ResponseEntity.ok(departments);

    }

}
