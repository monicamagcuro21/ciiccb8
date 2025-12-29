package com.bank.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// This annotation marks the class that defines the main configuration.
@SpringBootApplication
public class AppApplicationTests {

    public static void main(String[] args) {
        // This line starts the entire Spring Boot application context.
        SpringApplication.run(AppApplicationTests.class, args);
    }
}