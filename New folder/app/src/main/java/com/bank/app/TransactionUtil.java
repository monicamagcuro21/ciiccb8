package com.bank.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TransactionUtil {

    private static final String BANK_PREFIX = "NB";
    private static final Random RANDOM = new Random();

    public static String generateAccountNumber() {
        int randomPart = 10000000 + RANDOM.nextInt(90000000);
        return BANK_PREFIX + randomPart;
    }

    public static String generateReceipt(int typeCode, String accountId) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String accountSuffix = accountId.substring(Math.max(0, accountId.length() - 4));
        int randomSuffix = RANDOM.nextInt(100);
        return String.format("%s%d%s%02d", timestamp, typeCode, accountSuffix, randomSuffix);
    }
}
