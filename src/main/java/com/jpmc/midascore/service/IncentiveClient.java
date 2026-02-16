package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IncentiveClient {

    private static final Logger logger = LoggerFactory.getLogger(IncentiveClient.class);
    private static final String INCENTIVE_API_URL = "http://localhost:8080/incentive";

    private final RestTemplate restTemplate;

    public IncentiveClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Calls the external Incentive API to get the incentive amount for a transaction.
     *
     * @param transaction The transaction to get incentive for
     * @return Incentive object containing the incentive amount
     */
    public Incentive getIncentive(Transaction transaction) {
        try {
            logger.debug("Calling Incentive API for transaction: {}", transaction);
            
            // RestTemplate automatically:
            // 1. Serializes Transaction to JSON
            // 2. POSTs to the URL
            // 3. Receives JSON response
            // 4. Deserializes to Incentive object
            Incentive incentive = restTemplate.postForObject(
                    INCENTIVE_API_URL,
                    transaction,
                    Incentive.class
            );
            
            logger.debug("Received incentive: {}", incentive);
            return incentive;
            
        } catch (Exception e) {
            logger.error("Failed to call Incentive API: {}", e.getMessage());
            // Return zero incentive if API call fails
            return new Incentive(0.0f);
        }
    }
}