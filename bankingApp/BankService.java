package bankingApp;

public class BankService {

    public void deposit(User user, double amount) {
        double currentBalance = user.getBalance();
        user.deposit(amount);
        
        // Create and print receipt
        Transaction receipt = new Transaction("Deposit", user.getAccountNumber(), amount, user.getBalance());
        receipt.printReceipt(user.getName());
    }

    public void withdraw(User user, double amount) {
        if (user.withdraw(amount)) {
            // Create and print receipt
            Transaction receipt = new Transaction("Withdraw", user.getAccountNumber(), amount, user.getBalance());
            receipt.printReceipt(user.getName());
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    public void transfer(User sender, User receiver, double amount) {
        if (sender.withdraw(amount)) {
            receiver.deposit(amount);
            
            // Create and print two receipts
            // 1. Sender (Transfer Out)
            Transaction senderReceipt = new Transaction("Transfer_Out", sender.getAccountNumber(), amount, sender.getBalance());
            senderReceipt.printReceipt(sender.getName());

            // 2. Receiver (Transfer In) - Note: This transaction number will have a different sequence/suffix
            Transaction receiverReceipt = new Transaction("Transfer_In", receiver.getAccountNumber(), amount, receiver.getBalance());
            System.out.println("Transferred " + amount + " to " + receiver.getName());
        } else {
            System.out.println("Transfer failed. Not enough balance.");
        }
    }

    public void printBalance(User user) {
        System.out.println("Balance: " + String.format("%.2f", user.getBalance()));
    }
}