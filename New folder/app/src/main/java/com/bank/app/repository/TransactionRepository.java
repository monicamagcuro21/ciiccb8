package com.bank.app.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.bank.app.model.Transaction;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    // Finds all transactions where the account is either the source or destination, ordered by timestamp descending
    @Query(value = "{$or: [{ 'sourceAccountId': ?0 }, { 'destinationAccountId': ?0 }]}", sort = "{ 'timestamp': -1 }")
    List<Transaction> findHistoryByAccountIdOrderByTimestampDesc(String accountId);

    // Optional helper: find only outgoing transactions
    List<Transaction> findBySourceAccountIdOrderByTimestampDesc(String accountId);

    // Optional helper: find only incoming transactions
    List<Transaction> findByDestinationAccountIdOrderByTimestampDesc(String accountId);
}
