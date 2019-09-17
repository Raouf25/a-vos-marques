package com.sport;

import com.sport.service.FileService;
import com.sport.service.ScrapingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AVosMarquesApplication {

    public static void main(String[] args) {
        SpringApplication.run(AVosMarquesApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(FileService fileService, ScrapingService scrapingService) {
        return (args) -> {
            // only for first deployment
//            fileService.getAllByYear(2019);
//            scrapingService.getAllByYear(2019);
        };
    }

}
