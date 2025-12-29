package com.bank.app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transactions")
public class Transaction {

    @Id
    private String transactionId;
    private String sourceAccountId;
    private String destinationAccountId; 
    private BigDecimal amount;
    private String type;
    private LocalDateTime timestamp;

    // Constructor for new transactions
    public Transaction(String transactionId, String sourceAccountId, String destinationAccountId, BigDecimal amount, String type) {
        this.transactionId = transactionId;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now(); // Automatically set current timestamp
    }

    // Default constructor (required by MongoDB)
    public Transaction() {
        this.timestamp = LocalDateTime.now();
    }

    // --- Getters and Setters ---
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getSourceAccountId() { return sourceAccountId; }
    public void setSourceAccountId(String sourceAccountId) { this.sourceAccountId = sourceAccountId; }

    public String getDestinationAccountId() { return destinationAccountId; }
    public void setDestinationAccountId(String destinationAccountId) { this.destinationAccountId = destinationAccountId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    // --- Adapter / Convenience Getters ---
    public String getReceiptNumber() { return this.transactionId; }
    public String getTransactionType() { return this.type; }
    public String getRelatedAccount() { return this.destinationAccountId != null ? this.destinationAccountId : ""; }
    public String getAccountNumber() { return this.sourceAccountId; }
}
