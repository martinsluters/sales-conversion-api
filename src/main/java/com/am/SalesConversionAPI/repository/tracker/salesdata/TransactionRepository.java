package com.am.SalesConversionAPI.repository.tracker.salesdata;

import com.am.SalesConversionAPI.model.tracker.salesdata.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;

/**
 * Repository for the Transaction entity. This repository is used to interact with the database.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Builtin method to count the number of entries.
    long count();

    // Custom query to count the number of entries in a date range.
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.createdAt BETWEEN :startDate AND :endDate")
    long countInDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
