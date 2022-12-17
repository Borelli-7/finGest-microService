package com.kaly7dev.managemoneyqueryservice.services;

import com.kaly7dev.coreapi.TransactionCreatedEvent;
import com.kaly7dev.managemoneyqueryservice.entities.Transaction;
import com.kaly7dev.managemoneyqueryservice.repositories.TransactionRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class TransactionEventHandler {
    private TransactionRepo transactionRepo;

    @EventHandler
    public void on(TransactionCreatedEvent event){
        log.info("***************************");
        log.info("TransactionCreatedEvent received");
        Transaction transaction= new Transaction();
        transaction.setId(event.getId());
        transaction.setDesc(event.getDesc());
        transaction.setAmount(event.getAmount());
        transaction.setMonth(event.getMonth());
        transaction.setWeekNum(event.getWeek());
        transactionRepo.save(transaction);
    }
}
