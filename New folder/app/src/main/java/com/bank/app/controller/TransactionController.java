
package com.bank.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.app.BankingService;
import com.bank.app.model.Transaction;

@RestController
@RequestMapping("/api/v1/transactions") // Base URL: /api/v1/transactions
public class TransactionController {

    private final BankingService bankingService;

    @Autowired
    public TransactionController(BankingService bankingService) {
        this.bankingService = bankingService;
    }
    
    // =================================================================
    // DTOs for Transaction Requests
    // =================================================================
    public static class DepositRequest {
        public String accountId;
        public double amount;
    }
    
    public static class WithdrawRequest {
        public String accountId;
        public double amount;
    }
    
    // NEW DTO for Transfer Request (Matches the JSON sent from TransferForm.js)
    public static class TransferRequest {
        public String fromAccountId;
        public String toAccountId;
        public double amount;
        // Optionally, add a String description field here if needed.
    }

    // =================================================================
    // 1. DEPOSIT ENDPOINT (POST /api/v1/transactions/deposit)
    // =================================================================
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest request) {
        try {
            double newBalance = bankingService.deposit(request.accountId, request.amount);
            return ResponseEntity.ok(Map.of("message", "Deposit successful.", "newBalance", newBalance));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An unexpected error occurred during deposit."));
        }
    }


    // =================================================================
    // 2. WITHDRAW ENDPOINT (POST /api/v1/transactions/withdraw)
    // =================================================================
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawRequest request) {
        try {
            double newBalance = bankingService.withdraw(request.accountId, request.amount);
            return ResponseEntity.ok(Map.of("message", "Withdrawal successful.", "newBalance", newBalance));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An unexpected error occurred during withdrawal."));
        }
    }

    
    // =================================================================
    // 3. TRANSFER ENDPOINT (POST /api/v1/transactions/transfer) <-- NEW
    // =================================================================
    /**
     * Handles a fund transfer between two accounts. This requires a transactional approach
     * in the BankingService to ensure both debit and credit succeed or fail together.
     */
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        try {
            // NOTE: BankingService.transfer returns the sender's new balance.
            double senderNewBalance = bankingService.transfer(
                request.fromAccountId, 
                request.toAccountId, 
                request.amount
            );
            
            // Success response: returns the sender's new balance as expected by TransferForm.js
            return ResponseEntity.ok(Map.of(
                "message", "Transfer successful.", 
                "newBalance", senderNewBalance
            ));
        } catch (IllegalArgumentException e) {
            // Catch business logic errors (e.g., insufficient funds, invalid account IDs)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Catch unexpected server errors (e.g., database transaction failure)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An unexpected error occurred during transfer."));
        }
    }


    // =================================================================
    // 4. ACCOUNT BALANCE ENDPOINT (GET /api/v1/transactions/balance/{accountId})
    // =================================================================
    @GetMapping("/balance/{accountId}")
    public ResponseEntity<?> getAccountBalance(@PathVariable String accountId) {
        try {
            double balance = bankingService.getAccountBalance(accountId);
            return ResponseEntity.ok(Map.of("balance", balance));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("message", "Account not found or error fetching balance."));
        }
    }


    // =================================================================
    // 5. TRANSACTION HISTORY ENDPOINT (GET /api/v1/transactions/history/{accountId})
    // =================================================================
    @GetMapping("/history/{accountId}")
    public List<Transaction> getTransactionHistory(@PathVariable String accountId) {
        return bankingService.getTransactionHistory(accountId);
    }
}