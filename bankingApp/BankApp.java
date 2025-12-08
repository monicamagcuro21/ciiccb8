package bankingApp;
import java.util.Scanner;

public class BankApp {

    static Scanner sc = new Scanner(System.in);
    static BankService bankService = new BankService();

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n--- BANK SYSTEM ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose: ");

            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1 -> register();
                case 2 -> login();
                case 3 -> System.exit(0);
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ---------------- REGISTER ----------------
    public static void register() {
        System.out.print("Enter name: ");
        String name = sc.nextLine();

        System.out.print("Create account number: ");
        String acc = sc.nextLine();

        String pin;
        while (true) {
            System.out.print("Create 6-digit PIN: ");
            pin = sc.nextLine();

            if (pin.matches("\\d{6}")) break;
            System.out.println("PIN must be exactly 6 digits.");
        }

        User newUser = new User(name, acc, pin);
        Database.addUser(newUser);

        System.out.println("Registration successful!");
    }

    // ---------------- LOGIN ----------------
    public static void login() {
        System.out.print("Account Number: ");
        String acc = sc.nextLine();

        System.out.print("PIN: ");
        String pin = sc.nextLine();

        User user = Database.login(acc, pin);

        if (user == null) {
            System.out.println("Invalid credentials!");
            return;
        }

        System.out.println("\nWelcome, " + user.getName());
        userMenu(user);
    }

    // ---------------- USER MENU ----------------
    public static void userMenu(User user) {

        while (true) {
            System.out.println("\n--- USER MENU ---");
            System.out.println("1. Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Change PIN");
            System.out.println("6. Logout");
            System.out.print("Choose: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> bankService.printBalance(user);

                case 2 -> {
                    System.out.print("Enter amount: ");
                    bankService.deposit(user, sc.nextDouble());
                }

                case 3 -> {
                    System.out.print("Enter amount: ");
                    bankService.withdraw(user, sc.nextDouble());
                }

                case 4 -> {
                    sc.nextLine();
                    System.out.print("Receiver account #: ");
                    String receiverAcc = sc.nextLine();
                    User receiver = Database.findByAccount(receiverAcc);

                    if (receiver == null) {
                        System.out.println("Receiver not found.");
                        break;
                    }

                    System.out.print("Enter amount: ");
                    double amount = sc.nextDouble();
                    bankService.transfer(user, receiver, amount);
                }

                case 5 -> changePin(user);

                case 6 -> {
                    System.out.println("Logged out.");
                    return;
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ---------------- CHANGE PIN ----------------
    public static void changePin(User user) {
        sc.nextLine();
        System.out.print("Enter old PIN: ");
        String oldPin = sc.nextLine();

        if (!user.getPin().equals(oldPin)) {
            System.out.println("Incorrect old PIN.");
            return;
        }

        String newPin;
        while (true) {
            System.out.print("Enter new 6-digit PIN: ");
            newPin = sc.nextLine();
            if (newPin.matches("\\d{6}")) break;
            System.out.println("PIN must be exactly 6 digits.");
        }

        user.setPin(newPin);
        System.out.println("PIN updated successfully!");
    }
}

