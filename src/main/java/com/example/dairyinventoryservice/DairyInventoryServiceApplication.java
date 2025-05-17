package com.example.dairyinventoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DairyInventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DairyInventoryServiceApplication.class, args);
    }

}
