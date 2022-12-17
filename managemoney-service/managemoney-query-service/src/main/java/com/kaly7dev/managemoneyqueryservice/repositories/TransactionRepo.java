package com.kaly7dev.managemoneyqueryservice.repositories;

import com.kaly7dev.managemoneyqueryservice.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<Transaction, String> {

}
