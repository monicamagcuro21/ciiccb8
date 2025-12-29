package com.bank.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement; 

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.bank.app.repository")
@EnableTransactionManagement // Enables the @Transactional functionality (FIX for TransactionManager error)
public class BankApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }
}