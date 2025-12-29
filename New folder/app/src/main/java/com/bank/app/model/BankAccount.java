package com.bank.app.model; // CORRECTED PACKAGE

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accounts") // Added collection name for clarity
public class BankAccount {
    
    @Id 
    private String accountNumber;
    
    // Changed the data type from double to BigDecimal for accuracy
    private BigDecimal balance; 
    
    private String username; // Added username field for linking to UserCredentials

    // Constructor used by the application logic
    public BankAccount(String accountNumber, String username, BigDecimal initialBalance) {
        this.accountNumber = accountNumber;
        this.username = username;
        this.balance = initialBalance;
    }

    // Default constructor (required by Spring Data/Jackson)
    public BankAccount() {
        this.balance = BigDecimal.ZERO;
    }
    
    // Getters and Setters
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public String getUsername() { return username; }
    
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public void setUsername(String username) { this.username = username; }

    // Helper methods now correctly use BigDecimal
    public void deposit(BigDecimal amount) { 
        this.balance = this.balance.add(amount); 
    }
    
    public boolean withdraw(BigDecimal amount) {
        // Compare current balance with the amount to be withdrawn
        if (this.balance.compareTo(amount) >= 0) {
            this.balance = this.balance.subtract(amount);
            return true;
        }
        return false;
    }
    
    // Utility for safely creating BigDecimal from a double/string if needed later
    public static BigDecimal safeParse(String amount) {
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}