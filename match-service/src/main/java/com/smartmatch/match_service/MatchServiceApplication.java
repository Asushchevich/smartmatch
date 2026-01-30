package com.smartmatch.match_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.smartmatch.match_service", "com.smartmatch.common"})
@EnableCaching
@EnableScheduling
public class MatchServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatchServiceApplication.class, args);
    }
}
