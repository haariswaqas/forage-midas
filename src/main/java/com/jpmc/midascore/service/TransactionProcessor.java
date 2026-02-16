package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProcessor.class);

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final IncentiveClient incentiveClient;

    public TransactionProcessor(UserRepository userRepository,
                                TransactionRecordRepository transactionRecordRepository, 
                                IncentiveClient incentiveClient) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.incentiveClient = incentiveClient;
    }

    @Transactional
    public void processTransaction(Transaction transaction) {
        logger.info("Processing transaction: {}", transaction);

        // Step 1: Validate sender exists
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        if (sender == null) {
            logger.warn("Invalid transaction: Sender with ID {} not found", transaction.getSenderId());
            return;
        }

        // Step 2: Validate recipient exists
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());
        if (recipient == null) {
            logger.warn("Invalid transaction: Recipient with ID {} not found", transaction.getRecipientId());
            return;
        }

        // Step 3: Validate sender has sufficient funds
        if (sender.getBalance() < transaction.getAmount()) {
            logger.warn("Invalid transaction: Sender {} has insufficient funds. Balance: {}, Required: {}",
                    sender.getName(), sender.getBalance(), transaction.getAmount());
            return;
        }

        // Transaction is valid - process it
        logger.info("Valid transaction: {} (balance: {}) -> {} (balance: {}), amount: {}",
                sender.getName(), sender.getBalance(),
                recipient.getName(), recipient.getBalance(),
                transaction.getAmount());


        // Step 4: Get incentive from external API (NEW for Task 4)
        Incentive incentive = incentiveClient.getIncentive(transaction);
        float incentiveAmount = incentive.getAmount();
        logger.info("Incentive received: {}", incentiveAmount);


        // Step 5: Update balances
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

        // Step 5: Save updated user records
        userRepository.save(sender);
        userRepository.save(recipient);

        // Step 6: Create and save transaction record
        TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount(), incentiveAmount);
        transactionRecordRepository.save(record);

        logger.info("Transaction processed successfully. New balances - {}: {}, {}: {}",
                sender.getName(), sender.getBalance(),
                recipient.getName(), recipient.getBalance());
    }
}