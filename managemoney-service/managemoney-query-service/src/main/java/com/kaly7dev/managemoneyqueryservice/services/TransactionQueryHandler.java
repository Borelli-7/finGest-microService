package com.kaly7dev.managemoneyqueryservice.services;

import com.kaly7dev.coreapi.GetAllTransactionQuery;
import com.kaly7dev.coreapi.GetTransactionByIdQuery;
import com.kaly7dev.managemoneyqueryservice.entities.Transaction;
import com.kaly7dev.managemoneyqueryservice.repositories.TransactionRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TransactionQueryHandler {
    public TransactionRepo transactionRepo;

    @QueryHandler
    public List<Transaction> transactionList(GetAllTransactionQuery query){
        return transactionRepo.findAll();
    }

    @QueryHandler
    public Transaction transactionList(GetTransactionByIdQuery query){
        return transactionRepo.findById(query.getId()).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

}
