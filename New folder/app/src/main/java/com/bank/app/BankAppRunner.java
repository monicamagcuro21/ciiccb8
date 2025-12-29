package com.bank.app;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List; 
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.bank.app.model.BankAccount;
import com.bank.app.model.Transaction;
import com.bank.app.model.UserCredentials;
import com.bank.app.model.UserDetail;

@Component
public class BankAppRunner implements CommandLineRunner {

    private final BankingService bankingService;
    private final Scanner scanner = new Scanner(System.in);
    
    private UserCredentials loggedInUser = null;
    private BankAccount currentAccount = null;

    public BankAppRunner(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=============================================");
        System.out.println("🚀 Welcome to the Simple Bank CLI Application!");
        System.out.println("=============================================");
        
        mainApplicationLoop();
        
        scanner.close();
        System.out.println("\nApplication shutting down. Goodbye!");
    }

    private void mainApplicationLoop() {
        String choice;
        do {
            if (loggedInUser == null) {
                System.out.println("\n--- Auth Menu ---");
                System.out.println("1. Login");
                System.out.println("2. Register New User");
                System.out.println("6. Exit Application");
                System.out.print("Enter choice: ");
                choice = scanner.nextLine();
                
                switch (choice) {
                    case "1":
                        performLogin();
                        break;
                    case "2":
                        performRegistration();
                        break;
                    case "6":
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } else {
                showMainMenu();
                choice = "continue";
            }
        } while (true);
    }

    // --- Authentication & Registration ---

    private void performRegistration() {
        System.out.println("\n==========New Registration===========");
        
        String suffix = "", firstName = "", middleName = "", lastName = "";
        String username = "", password = "";
        String birthdateStr = "";
        String houseNoStreet = "", district = "", cityMunicipality = "", state = "";
        
        try {
            System.out.print("Enter Suffix (Optional): ");
            suffix = scanner.nextLine().trim();

            System.out.print("*Enter First Name: ");
            firstName = scanner.nextLine().trim();
            if (firstName.isEmpty()) { throw new IllegalArgumentException("First Name is required."); }
            
            System.out.print("Enter Middle Name (Optional): ");
            middleName = scanner.nextLine().trim();
            
            System.out.print("*Enter Last Name: ");
            lastName = scanner.nextLine().trim();
            if (lastName.isEmpty()) { throw new IllegalArgumentException("Last Name is required."); }

            System.out.print("*Enter Username: ");
            username = scanner.nextLine().trim();
            if (username.isEmpty()) { throw new IllegalArgumentException("Username is required."); }
            
            System.out.print("*Enter Password: ");
            password = scanner.nextLine();
            if (password.isEmpty()) { throw new IllegalArgumentException("Password is required."); }

            System.out.print("*Enter Birthdate (YYYY-MM-DD): ");
            birthdateStr = scanner.nextLine();
            LocalDate birthdate = LocalDate.parse(birthdateStr);
            
            // --- ADDRESS FIELDS ---
            System.out.print("*Enter House No. & Street: ");
            houseNoStreet = scanner.nextLine().trim();
            if (houseNoStreet.isEmpty()) { throw new IllegalArgumentException("House No. & Street is required."); }
            
            System.out.print("*Enter District: ");
            district = scanner.nextLine().trim();
            if (district.isEmpty()) { throw new IllegalArgumentException("District is required."); }
            
            System.out.print("*Enter City / Municipality: ");
            cityMunicipality = scanner.nextLine().trim();
            if (cityMunicipality.isEmpty()) { throw new IllegalArgumentException("City is required."); }
            
            System.out.print("*Enter State: ");
            state = scanner.nextLine().trim();
            if (state.isEmpty()) { throw new IllegalArgumentException("State is required."); }
            // --- END OF ADDRESS FIELDS ---
            
            System.out.println("===========End of Registration=========");
            
            int age = Period.between(birthdate, LocalDate.now()).getYears(); 
            System.out.println("(Calculated Age: " + age + ")\n");
            
            UserCredentials newUser = bankingService.registerNewUser(
                username, password,
                firstName, middleName, lastName, suffix, birthdateStr, 
                houseNoStreet, district, cityMunicipality, state
            );
            
            if (newUser != null) {
                loggedInUser = newUser;
                currentAccount = bankingService.getAccount(newUser.getAccountNumber());
                
                UserDetail userDetails = bankingService.getUserDetails(username);
                
                System.out.println("=========================================");
                System.out.println("🚀 NEW ACCOUNT CONFIRMATION RECEIPT");
                System.out.println("=========================================");
                System.out.println("Date Account Made: " + LocalDate.now().format(DateTimeFormatter.ISO_DATE));
                System.out.println("Account Number: " + currentAccount.getAccountNumber());
                System.out.println("-----------------------------------------");
                System.out.println("NAME DETAILS:");
                System.out.println("Username: " + newUser.getUsername());
                
                if (userDetails != null) {
                    System.out.println("Full Name: " + (userDetails.getFirstName() + " " + (userDetails.getMiddleName().isEmpty() ? "" : userDetails.getMiddleName() + " ") + userDetails.getLastName() + " " + userDetails.getSuffix()).trim());
                    System.out.println("Birthdate: " + userDetails.getBirthdate()); 
                    
                    System.out.println("-----------------------------------------");
                    System.out.println("ADDRESS:");
                    System.out.println("Address No. & Street: " + userDetails.getHouseNoStreet());
                    System.out.println("District: " + userDetails.getDistrict());
                    System.out.println("City: " + userDetails.getCityMunicipality());
                    System.out.println("State: " + userDetails.getState());
                }
                
                System.out.println("=========================================");
                System.out.println("> Registration successful! Welcome, " + username + "!");

            } 

        } catch (IllegalArgumentException e) {
            System.out.println("> Registration failed: " + e.getMessage());
        } catch (java.time.format.DateTimeParseException e) {
            System.out.println("> Registration failed: Invalid date format. Please use YYYY-MM-DD.");
        } catch (Exception e) {
            System.out.println("> An unexpected error occurred during registration: " + e.getMessage());
        }
    }
    
    private void performLogin() {
        System.out.println("\n--- Login ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        UserCredentials user = bankingService.authenticate(username, password);

        if (user != null) {
            loggedInUser = user;
            currentAccount = bankingService.getAccount(user.getAccountNumber());
            
            UserDetail details = bankingService.getUserDetails(username);
            String welcomeName = (details != null) ? details.getFirstName() : loggedInUser.getUsername();
            
            System.out.println("\n> Login successful! Welcome, " + welcomeName + ".");
        } else {
            System.out.println("\n> Login failed. Invalid username or password.");
        }
    }

    private void performLogout() {
        System.out.println("Goodbye, " + loggedInUser.getUsername() + "!");
        bankingService.logout(loggedInUser.getUsername()); 
        
        loggedInUser = null;
        currentAccount = null;
    }
    
    // --- Main Menu and Transactions ---

    private void showMainMenu() {
        String choice;
        System.out.println("\n--- Transaction Menu (User: " + loggedInUser.getUsername() + ") ---");
        System.out.println("1. Withdraw");
        System.out.println("2. Check Balance");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Logout");
        System.out.println("6. Transaction History");
        System.out.print("Enter choice: ");
        choice = scanner.nextLine();

        try {
            switch (choice) {
                case "1":
                    performWithdrawal();
                    break;
                case "2":
                    double currentBalance = bankingService.getAccountBalance(currentAccount.getAccountNumber());
                    System.out.printf("\nBalance for account %s: $%,.2f%n", 
                        currentAccount.getAccountNumber(), currentBalance);
                    break;
                case "3":
                    performDeposit();
                    break;
                case "4":
                    performTransfer();
                    break;
                case "5":
                    performLogout();
                    break;
                case "6":
                    viewTransactionHistory();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("> Invalid input. Please enter a valid number for amount.");
        } catch (IllegalArgumentException e) {
            // Catching exceptions thrown by the BankingService logic
            System.out.println("Transaction failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
    
    private void performDeposit() {
        try {
            System.out.print("Enter amount to deposit: $");
            double amount = Double.parseDouble(scanner.nextLine());
            
            double newBalance = bankingService.deposit(currentAccount.getAccountNumber(), amount); 
            System.out.printf("> Deposit successful. New balance: $%,.2f%n", newBalance);
        } catch (NumberFormatException e) {
            System.out.println("> Deposit failed: Please enter a valid numeric amount.");
        }
    }

    private void performWithdrawal() {
        try {
            System.out.print("Enter amount to withdraw: $");
            double amount = Double.parseDouble(scanner.nextLine());
            
            double newBalance = bankingService.withdraw(currentAccount.getAccountNumber(), amount); 
            System.out.printf("> Withdrawal successful. New balance: $%,.2f%n", newBalance);
        } catch (NumberFormatException e) {
            System.out.println("> Withdrawal failed: Please enter a valid numeric amount.");
        }
    }

    private void performTransfer() {
        try {
            System.out.print("Enter recipient account number: ");
            String recipientAccNum = scanner.nextLine();
            System.out.print("Enter amount to transfer: $");
            double amount = Double.parseDouble(scanner.nextLine());
            
            double newBalance = bankingService.transfer(currentAccount.getAccountNumber(), recipientAccNum, amount);
            System.out.printf("> Transfer successful. Your new balance: $%,.2f%n", newBalance);
        } catch (NumberFormatException e) {
            System.out.println("> Transfer failed: Please enter a valid numeric amount.");
        }
    }
    
    private void viewTransactionHistory() {
        System.out.println("\nGetting Transaction....");
        System.out.println("Please wait...");

        System.out.println("\n--- Transaction History for Account " + currentAccount.getAccountNumber() + " ---");
        List<Transaction> history = bankingService.getTransactionHistory(currentAccount.getAccountNumber());
        
        if (history.isEmpty()) {
            System.out.println("No transactions found for this account.");
            return;
        }

        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("| %-20s | %-12s | %-15s | %-15s | %-12s |\n",
            "Date/Time", "Type", "Amount", "Related Acc", "Receipt");
        System.out.println("--------------------------------------------------------------------------------");

        for (Transaction t : history) {
            // Check if amount is negative, and format it using BigDecimal
            BigDecimal amount = t.getAmount().setScale(2, RoundingMode.HALF_UP);
            String amountStr = String.format("$%s", amount.toPlainString());
            
            String relatedAcc = t.getRelatedAccount() != null && !t.getRelatedAccount().isEmpty() ? t.getRelatedAccount() : "---";
            String receipt = t.getReceiptNumber() != null ? t.getReceiptNumber() : "N/A";

            System.out.printf("| %-20s | %-12s | %-15s | %-15s | %-12s |\n",
                t.getTimestamp().toLocalDate() + " " + t.getTimestamp().toLocalTime().withNano(0),
                t.getTransactionType(),
                amountStr,
                relatedAcc,
                receipt
                );
        }
        System.out.println("--------------------------------------------------------------------------------");
    }
}