package com.pharmacy.supplychain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartPharmacyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartPharmacyApplication.class, args);
        System.out.println("================================================================");
        System.out.println("  Smart Pharmacy Supply Chain System Backend Started Successfully!");
        System.out.println("  REST API Base URL: http://localhost:8080/api");
        System.out.println("  H2 Console (if local profile): http://localhost:8080/h2-console");
        System.out.println("================================================================");
    }
}
