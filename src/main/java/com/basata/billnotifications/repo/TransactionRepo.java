package com.basata.billnotifications.repo;

import com.basata.billnotifications.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
}
