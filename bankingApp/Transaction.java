package bankingApp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String transactionNumber;
    private String type;
    private double amount;
    private double postBalance;

    public Transaction(String type, String accountNumber, double amount, double postBalance) {
        this.type = type;
        this.amount = amount;
        this.postBalance = postBalance;
        this.transactionNumber = generateTransactionNumber(type, accountNumber);
    }

    private String generateTransactionNumber(String type, String accountNumber) {
        // Map transaction type to a two-digit code
        String typeCode = switch (type.toLowerCase()) {
            case "deposit" -> "01";
            case "withdraw" -> "03";
            case "transfer_out" -> "04";
            case "transfer_in" -> "05";
            default -> "99";
        };

        // Format Date (MMddyyyy)
        String dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("MMddyyyy"));

        // Get last 3 digits of account number
        String accountSuffix = accountNumber.substring(accountNumber.length() - 3);

        // Get sequential number (This requires a global counter, which we'll add to the Database class)
        // For now, we'll use a placeholder and rely on the Database to manage the sequence.
        String seq = String.format("%02d", Database.getNextSequence());

        // Format: TYPECODE-MMddyyyy-SUFFIXSEQ (e.g., 03-12092025-12300)
        return typeCode + "-" + dateString + "-" + accountSuffix + seq;
    }

    public void printReceipt(String userName) {
        System.out.println("\n--- TRANSACTION RECEIPT ---");
        System.out.println("User: " + userName);
        System.out.println("Transaction: " + transactionNumber);
        System.out.println("Type: " + type.toUpperCase());
        System.out.println("Amount: " + String.format("%.2f", amount));
        System.out.println("New Balance: " + String.format("%.2f", postBalance));
        System.out.println("---------------------------\n");
    }
}