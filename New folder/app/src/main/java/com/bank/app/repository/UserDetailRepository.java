// src/main/java/com/bank/app/repository/UserDetailRepository.java
package com.bank.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bank.app.model.UserDetail;

@Repository
public interface UserDetailRepository extends MongoRepository<UserDetail, String> {
    // Optional: you can add custom query methods here if needed, e.g.:
    // Optional<UserDetail> findByUsername(String username);
}
