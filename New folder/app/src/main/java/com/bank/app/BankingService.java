package com.bank.app;

import com.bank.app.model.BankAccount;
import com.bank.app.model.UserCredentials; 
import com.bank.app.model.Transaction;
import com.bank.app.model.UserDetail;
import com.bank.app.repository.AccountRepository;
import com.bank.app.repository.TransactionRepository;
import com.bank.app.repository.UserDetailRepository;
import com.bank.app.repository.UserRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class BankingService {

    private static final BigDecimal MAX_DEPOSIT_LIMIT = new BigDecimal("1000000.00");
    private static final BigDecimal MINIMUM_REMAINING_BALANCE = BigDecimal.ZERO; 

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserDetailRepository userDetailRepository;
    private final PasswordEncoder passwordEncoder;

    public BankingService(
        UserRepository userRepository, 
        AccountRepository accountRepository, 
        TransactionRepository transactionRepository,
        UserDetailRepository userDetailRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userDetailRepository = userDetailRepository;
        this.passwordEncoder = passwordEncoder;
        
        System.out.println("\n🏦 BankingService ready. Total users: " + userRepository.count());
    }

    // =================================================================
    // --- UTILITY METHODS ---
    // =================================================================
    public BankAccount getAccount(String accountNumber) {
        return accountRepository.findById(accountNumber).orElse(null);
    }
    
    public void logout(String username) {
        System.out.println("User " + username + " session cleared.");
    }

    // =================================================================
    // --- AUTH & REGISTRATION ---
    // =================================================================
    @Transactional
    public UserCredentials registerNewUser(
        String username, String password, 
        String firstName, String middleName, String lastName, String suffix, 
        String birthdateStr, 
        String houseNoStreet, String district, String cityMunicipality, String state
    ) throws IllegalArgumentException { 
        
        if (userRepository.existsById(username)) {
            throw new IllegalArgumentException("Registration failed: Username already exists.");
        }

        LocalDate birthdate = LocalDate.parse(birthdateStr, DateTimeFormatter.ISO_DATE);
        LocalDate eighteenYearsAgo = LocalDate.now().minusYears(18);
        if (birthdate.isAfter(eighteenYearsAgo)) {
            throw new IllegalArgumentException("Registration failed: Applicant must be 18 years or older.");
        }
        
        String newAccNum = TransactionUtil.generateAccountNumber(); 
        BankAccount account = new BankAccount(newAccNum, username, BigDecimal.ZERO); 
        
        String hashedPassword = passwordEncoder.encode(password);
        UserCredentials user = new UserCredentials(username, hashedPassword, newAccNum);
        userRepository.save(user);
        
        UserDetail details = new UserDetail();
        details.setUsername(username);
        details.setFirstName(firstName);
        details.setMiddleName(middleName);
        details.setLastName(lastName);
        details.setSuffix(suffix);
        details.setBirthdate(birthdateStr); 
        details.setHouseNoStreet(houseNoStreet);
        details.setDistrict(district);
        details.setCityMunicipality(cityMunicipality);
        details.setState(state);

        userDetailRepository.save(details);
        accountRepository.save(account);
        
        return user; 
    }
    
    public UserDetail getUserDetails(String username) {
        return userDetailRepository.findById(username).orElse(null);
    }
    
    public UserCredentials authenticate(String username, String password) {
       Optional<UserCredentials> userOptional = userRepository.findById(username); 
if (userOptional.isPresent()) {
    UserCredentials user = userOptional.get();
    if (passwordEncoder.matches(password, user.getPasswordHash())) { 
        return user;
    }
}

        return null;
    }

    // =================================================================
    // --- ACCOUNT/BALANCE METHODS ---
    // =================================================================
    public double getAccountBalance(String accountNumber) throws IllegalArgumentException {
        BankAccount account = getAccount(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found.");
        }
        return account.getBalance().setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    // =================================================================
    // --- TRANSACTION METHODS ---
    // =================================================================
    @Transactional
    public double deposit(String accountNumber, double amount) throws IllegalArgumentException {
        BankAccount account = getAccount(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Deposit failed: Account not found.");
        }
        
        BigDecimal depositAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);

        if (depositAmount.compareTo(BigDecimal.ZERO) <= 0 || depositAmount.compareTo(MAX_DEPOSIT_LIMIT) > 0) {
            throw new IllegalArgumentException(String.format("Deposit failed: Amount must be positive and under $%,.2f.", MAX_DEPOSIT_LIMIT));
        }
        
        account.setBalance(account.getBalance().add(depositAmount));
        accountRepository.save(account); 
        
        String receipt = TransactionUtil.generateReceipt(3, account.getAccountNumber());
        Transaction transaction = new Transaction(receipt, account.getAccountNumber(), null, depositAmount, "DEPOSIT");
        transactionRepository.save(transaction);
        
        return account.getBalance().setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    @Transactional
    public double withdraw(String accountNumber, double amount) throws IllegalArgumentException {
        BankAccount account = getAccount(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Withdrawal failed: Account not found.");
        }
        
        BigDecimal withdrawAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP).abs();

        if (withdrawAmount.compareTo(BigDecimal.ZERO) <= 0) {
             throw new IllegalArgumentException("Withdrawal failed: Amount must be positive.");
        }

        if (account.getBalance().compareTo(withdrawAmount.add(MINIMUM_REMAINING_BALANCE)) < 0) {
            throw new IllegalArgumentException(String.format("Withdrawal failed: Insufficient funds. Available: $%,.2f",
                account.getBalance()));
        }
        
        account.setBalance(account.getBalance().subtract(withdrawAmount));
        accountRepository.save(account); 
        
        String receipt = TransactionUtil.generateReceipt(1, account.getAccountNumber());
        Transaction transaction = new Transaction(receipt, account.getAccountNumber(), null, withdrawAmount.negate(), "WITHDRAWAL");
        transactionRepository.save(transaction);
        
        return account.getBalance().setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    @Transactional
    public double transfer(String fromAccNum, String toAccNum, double amount) throws IllegalArgumentException {
        BankAccount fromAccount = getAccount(fromAccNum);
        BankAccount toAccount = getAccount(toAccNum);
        
        BigDecimal transferAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP).abs();

        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Transfer failed: One or both accounts not found.");
        }
        if (fromAccNum.equals(toAccNum)) {
            throw new IllegalArgumentException("Transfer failed: Cannot transfer funds to the same account.");
        }
        if (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
             throw new IllegalArgumentException("Transfer failed: Amount must be positive.");
        }

        if (fromAccount.getBalance().compareTo(transferAmount.add(MINIMUM_REMAINING_BALANCE)) < 0) {
            throw new IllegalArgumentException(String.format("Transfer failed: Insufficient funds in account %s. Available: $%,.2f", 
                fromAccNum, fromAccount.getBalance()));
        }
        
        fromAccount.setBalance(fromAccount.getBalance().subtract(transferAmount));
        toAccount.setBalance(toAccount.getBalance().add(transferAmount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        String receipt = TransactionUtil.generateReceipt(4, fromAccNum);
        Transaction senderTrans = new Transaction(receipt + "S", fromAccNum, toAccNum, transferAmount.negate(), "TRANSFER_SENT");
        transactionRepository.save(senderTrans);
        Transaction recipientTrans = new Transaction(receipt + "R", toAccNum, fromAccNum, transferAmount, "TRANSFER_RECEIVED");
        transactionRepository.save(recipientTrans);
        
        return fromAccount.getBalance().setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    // --- HISTORY METHOD ---
    public List<Transaction> getTransactionHistory(String accountNumber) {
        return transactionRepository.findHistoryByAccountIdOrderByTimestampDesc(accountNumber);
    }
}
