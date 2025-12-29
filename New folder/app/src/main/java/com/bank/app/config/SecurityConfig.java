package com.bank.app.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- CRITICAL FIX: ADD THIS IMPORT

@Configuration
public class SecurityConfig {

    /**
     * FIX: Temporarily defines NoOpPasswordEncoder for development.
     * This allows the application to successfully compare the plain text 
     * password entered by the user with the plain text password stored in 
     * the MongoDB 'users' collection.
     * * NOTE: This is NOT safe for production! Always use BCrypt in production.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    
    // NOTE: For a complete Spring Security setup, you would typically add 
    // a SecurityFilterChain bean here to configure CORS, CSRF, and authorization rules.
}