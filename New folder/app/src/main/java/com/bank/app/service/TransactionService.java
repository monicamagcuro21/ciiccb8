package com.bank.app.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.app.TransactionUtil;
import com.bank.app.model.Transaction;
import com.bank.app.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public Transaction createTransaction(String source, String dest, BigDecimal amount, String type) {
        int typeCode = getTypeCode(type);
        String receipt = TransactionUtil.generateReceipt(typeCode, source);

        Transaction tx = new Transaction(receipt, source, dest, amount, type);
        return transactionRepository.save(tx);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionHistory(String accountId) {
        return transactionRepository.findHistoryByAccountIdOrderByTimestampDesc(accountId);
    }

    private int getTypeCode(String type) {
        switch(type.toLowerCase()) {
            case "withdraw": return 1;
            case "balance": return 2;
            case "deposit": return 3;
            case "transfer": return 4;
            default: return 0;
        }
    }
}
