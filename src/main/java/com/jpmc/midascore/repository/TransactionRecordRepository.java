package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.TransactionRecord;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRecordRepository extends CrudRepository<TransactionRecord, Long> {
    // Spring Data JPA will provide implementations for:
    // - save(TransactionRecord)
    // - findById(Long)
    // - findAll()
    // - delete(TransactionRecord)
    // etc.
}