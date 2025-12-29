// BankService.java (UPDATED for MongoDB and Receipts)



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class BankService {

    // Inject the MongoDB Repository
    private final UserRepository userRepository;

    // Constructor Injection (Spring Boot handles this)
    public BankService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // --- Helper for Transaction Number Generation ---
    // Note: This is a placeholder; a robust system would use a distributed ID generator.
    private String generateTransactionNumber(String typeCode, String accountNumber) {
        String dateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddyyyyHHmmss"));
        String accountSuffix = accountNumber.substring(accountNumber.length() - 3);
        // Simplified structure: TYPE-MMDDYYYYHHMMSS-SUFFIX
        return typeCode + "-" + dateString + "-" + accountSuffix;
    }

    // -----------------------------------------------------------------------------------
    
    // 1. DEPOSIT
    @Transactional
    public void deposit(String accountNumber, double amount) {
        Optional<User> userOpt = userRepository.findById(accountNumber);
        if (userOpt.isEmpty()) {
            System.out.println("Account not found.");
            return;
        }

        User user = userOpt.get();
        user.deposit(amount);
        
        // Create Receipt
        String txNum = generateTransactionNumber("01", accountNumber);
        TransactionReceipt receipt = new TransactionReceipt(
            txNum, "Deposit", amount, user.getBalance(), LocalDateTime.now().toString()
        );
        user.addTransaction(receipt);
        userRepository.save(user); // Persist changes to MongoDB

        System.out.println("Deposited: " + amount);
        System.out.println("Receipt: " + txNum);
    }
    
    // 2. WITHDRAW
    @Transactional
    public void withdraw(String accountNumber, double amount) {
        Optional<User> userOpt = userRepository.findById(accountNumber);
        if (userOpt.isEmpty()) {
            System.out.println("Account not found.");
            return;
        }

        User user = userOpt.get();
        if (user.withdraw(amount)) {
            // Create Receipt
            String txNum = generateTransactionNumber("03", accountNumber);
            TransactionReceipt receipt = new TransactionReceipt(
                txNum, "Withdraw", amount, user.getBalance(), LocalDateTime.now().toString()
            );
            user.addTransaction(receipt);
            userRepository.save(user); // Persist changes to MongoDB

            System.out.println("Withdrawn: " + amount);
            System.out.println("Receipt: " + txNum);
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    // You would follow a similar pattern for register and transfer methods.
    
    public void printBalance(String accountNumber) {
        userRepository.findById(accountNumber).ifPresentOrElse(
            user -> System.out.println("Balance: " + String.format("%.2f", user.getBalance())),
            () -> System.out.println("Account not found.")
        );
    }

    // NOTE: For Transfer, you would need two saves (sender and receiver)
    // and two separate receipts ("Transfer_Out" and "Transfer_In").
}