// BankingApplication.java (NEW FILE)


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// This annotation tells Spring Boot where to start the application
@SpringBootApplication 
public class BankingApplication {

    public static void main(String[] args) {
        // This command launches the entire Spring Boot context
        SpringApplication.run(BankingApplication.class, args);
        System.out.println("Spring Boot Bank Application started!");
    }
}