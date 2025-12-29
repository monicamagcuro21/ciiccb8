package com.bank.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    // 1️⃣ Define MongoClient as a bean
    @Bean
    public MongoClient mongoClient() {
        ConnectionString mongoUri = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(mongoUri)
                .build();
        return MongoClients.create(settings);
    }

    // 2️⃣ Define TransactionManager bean
    @Bean
    public MongoTransactionManager transactionManager(MongoClient mongoClient) {
        // Updated database name to match your Atlas DB
        return new MongoTransactionManager(
                new SimpleMongoClientDatabaseFactory(mongoClient, "Database01")
        );
    }
}
