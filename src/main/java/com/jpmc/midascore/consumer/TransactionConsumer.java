package com.jpmc.midascore.consumer;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionConsumer.class);
    
    private final TransactionProcessor transactionProcessor;
    
    public TransactionConsumer(TransactionProcessor transactionProcessor) {
        this.transactionProcessor = transactionProcessor;
    }
    
    @KafkaListener(topics = "${general.kafka-topic}")
    public void receiveTransaction(Transaction transaction) {
        logger.info("Received transaction: {}", transaction);
        
        // Process the transaction (validate and update database)
        transactionProcessor.processTransaction(transaction);
    }
}