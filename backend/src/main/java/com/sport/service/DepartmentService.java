package com.sport.service;

import com.sport.entity.Region;
import com.sport.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DepartmentService {

    @Autowired
    RegionRepository regionRepository;

    public List<String> getAll() {
        return StreamSupport
                .stream(regionRepository.findAll().spliterator(), false)
                .map(Region::getCode)
                .filter(code -> !code.equals("2A") && !code.equals("2B"))
                .map(code -> "00".concat(code).substring("00".concat(code).length() - 3))  // to get three digit
                .collect(Collectors.toList());

    }

}
