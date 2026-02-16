package com.jpmc.midascore.consumer;

import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionConsumer.class);
    
    @KafkaListener(topics = "${general.kafka-topic}")
    public void receiveTransaction(Transaction transaction) {
        logger.info("Received transaction: {}", transaction);
        // TODO: Process transaction in Task 3
        // For now, we're just receiving and logging the transactions
    }
}