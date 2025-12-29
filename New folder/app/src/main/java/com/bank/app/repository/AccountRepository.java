// File path should be: src/main/java/com/bank/app/repository/AccountRepository.java

package com.bank.app.repository; // <--- FIX: MUST BE IN 'repository' PACKAGE

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bank.app.model.BankAccount; // <--- This import is now correct relative to BankingService

@Repository
public interface AccountRepository extends MongoRepository<BankAccount, String> {
}