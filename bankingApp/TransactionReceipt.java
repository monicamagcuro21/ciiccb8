


// Note: No Spring annotations needed here as it is embedded in the User document.
public class TransactionReceipt {
    private String transactionNumber;
    private String type; // e.g., "Deposit", "Withdraw", "Transfer_Out"
    private double amount;
    private double postBalance; // Balance after the transaction
    private String timestamp; // Date and time of the transaction

    // Constructor 
    public TransactionReceipt(String transactionNumber, String type, double amount, double postBalance, String timestamp) {
        this.transactionNumber = transactionNumber;
        this.type = type;
        this.amount = amount;
        this.postBalance = postBalance;
        this.timestamp = timestamp;
    }

    // Getters
    public String getTransactionNumber() { return transactionNumber; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getPostBalance() { return postBalance; }
    public String getTimestamp() { return timestamp; }
    
    // Setters (optional, but good practice if needed)
    // ...
}