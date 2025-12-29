


import java.util.ArrayList;

public class Database {
    private static ArrayList<User> users = new ArrayList<>();
    // New static field to track the sequential part of the transaction number
    private static int transactionSequence = 0; 

    public static void addUser(User user) {
        users.add(user);
    }

    public static User findByAccount(String accountNumber) {
        for (User user : users) {
            if (user.getAccountNumber().equals(accountNumber)) {
                return user;
            }
        }
        return null;
    }

    public static User login(String accountNumber, String pin) {
        for (User user : users) {
            if (user.getAccountNumber().equals(accountNumber) &&
                user.getPin().equals(pin)) {
                return user;
            }
        }
        return null;
    }

    // New static method to get and increment the sequential number
    public static int getNextSequence() {
        if (transactionSequence >= 99) {
            transactionSequence = 0; // Reset after 99 for the two-digit format
        }
        return transactionSequence++;
    }
}