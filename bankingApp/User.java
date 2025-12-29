

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

// 1. @Document maps this class to the 'users' collection in MongoDB.
// 2. @CompoundIndex creates a fast lookup index for the login query (accountNumber + pin).
@Document(collection = "users")
@CompoundIndex(name = "login_idx", def = "{'accountNumber': 1, 'pin': 1}") 
public class User {
    
    // @Id marks the field that will be used as the primary key (_id) in MongoDB.
    @Id 
    private String accountNumber;
    
    private String name;
    private double balance;
    private String pin; // 6-digit PIN

    // 3. Embedded list for recent transaction history (Optimization)
    private List<TransactionReceipt> transactionHistory = new ArrayList<>();

    // Constructor remains largely the same
    public User(String name, String accountNumber, String pin) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = 0; // default
        // transactionHistory is initialized above
    }

    // ========== GETTERS AND SETTERS ==========

    public String getName() {
        return name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }
    
    // NOTE: Added setter for balance, often needed when retrieving from DB initially
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String newPin) {
        this.pin = newPin;
    }
    
    public List<TransactionReceipt> getTransactionHistory() {
        return transactionHistory;
    }

    // ========== TRANSACTION METHODS ==========

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
    
    /**
     * Adds a new transaction receipt to the history.
     * Keeps the list size manageable (e.g., last 10) for performance.
     */
    public void addTransaction(TransactionReceipt receipt) {
        // Add the new receipt to the beginning of the list
        this.transactionHistory.add(0, receipt); 
        
        // Remove the oldest transaction if the list exceeds 10 items
        if (this.transactionHistory.size() > 10) {
            this.transactionHistory.remove(10);
        }
    }
}