package com.bank.app.controller;

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
import com.bank.app.model.UserCredentials; // Import the model
import com.bank.app.model.UserDetail;

@RestController
@RequestMapping("/api/v1/users") // Base URL: /api/v1/users
public class UserController {

    private final BankingService bankingService;

    @Autowired
    public UserController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    // =================================================================
    // DTOs (Data Transfer Objects) for API requests
    // =================================================================

    // DTO for Login Request (Matches the JSON sent from Login.js)
    public static class LoginRequest {
        public String username;
        public String password;
    }
    
    // DTO for Registration Request
    public static class RegistrationRequest {
        // Core User fields
        public String username;
        public String password;
        public String firstName;
        public String middleName; 
        public String lastName;
        public String suffix; 
        public String birthdate; 
        
        // Address fields
        public String houseNoStreet;
        public String district;
        public String cityMunicipality;
        public String state;
    }

    // =================================================================
    // 1. LOGIN ENDPOINT (POST /api/v1/users/login)
    // =================================================================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // FIX: Changed login() to authenticate() to match the refactored BankingService method
            UserCredentials authenticatedUser = bankingService.authenticate(request.username, request.password); 

            if (authenticatedUser != null) {
                String accountNumber = authenticatedUser.getAccountNumber();
                
                // Success: Return account number AND the username
                return ResponseEntity.ok(Map.of(
                    "accountNumber", accountNumber,
                    "username", request.username // Return username from the request DTO
                ));
            } else {
                // Failure: 401 Unauthorized status
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid username or password."));
            }
        } catch (Exception e) {
            // Server error handling
            // Log the error for debugging (not shown here)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An unexpected error occurred during login."));
        }
    }

    // =================================================================
    // 2. REGISTRATION ENDPOINT (POST /api/v1/users/register)
    // =================================================================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        try {
            bankingService.registerNewUser(
                request.username, 
                request.password, 
                request.firstName, 
                request.middleName, 
                request.lastName, 
                request.suffix,      
                request.birthdate,
                request.houseNoStreet, 
                request.district,      
                request.cityMunicipality, 
                request.state          
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User registered successfully."));
        } catch (Exception e) {
            // Catches exceptions like a username already being taken or Age Validation failure
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    // =================================================================
    // 3. GET USER DETAILS ENDPOINT (GET /api/v1/users/details/{username})
    // =================================================================
    /**
     * Retrieves extended details for a user (used for Profile view).
     */
    @GetMapping("/details/{username}")
    public ResponseEntity<?> getUserDetails(@PathVariable String username) {
        try {
            // Assuming BankingService.getUserDetails returns the UserDetail object
            UserDetail details = bankingService.getUserDetails(username);
            
            if (details != null) {
                // Success: Return the UserDetail object (Spring will convert it to JSON)
                return ResponseEntity.ok(details);
            } else {
                // Failure: 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User details not found for username: " + username));
            }
        } catch (Exception e) {
            // Server error handling
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An unexpected error occurred while fetching user details."));
        }
    }

    // =================================================================
    // 4. LOGOUT ENDPOINT (POST /api/v1/users/logout)
    // =================================================================
    /**
     * Simple endpoint to acknowledge client-side logout/session termination.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logout acknowledged. Session cleared on client side."));
    }
}