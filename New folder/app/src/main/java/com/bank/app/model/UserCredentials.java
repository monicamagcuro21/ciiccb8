package com.bank.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users") // Explicitly maps this model to the 'users' collection
public class UserCredentials {
    
    @Id
    private String username;
    
    private String passwordHash; 
    private String accountNumber;

    public UserCredentials() {}

    public UserCredentials(String username, String passwordHash, String accountNumber) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.accountNumber = accountNumber;
    }

    // --- Getters and Setters ---
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
