package com.smartcity.wastemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.smartcity.wastemanagement.model")
@EnableJpaRepositories("com.smartcity.wastemanagement.repository")
public class WasteManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(WasteManagementApplication.class, args);
    }
}