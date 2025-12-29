package com.bank.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bank.app.model.UserCredentials; // Make sure this import points to the new model

@Repository
public interface UserRepository extends MongoRepository<UserCredentials, String> {
    // Now it works with UserCredentials instead of User
}
