package com.starters.applyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ApplyserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplyserviceApplication.class, args);
    }

}
