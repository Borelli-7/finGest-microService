package com.kaly7dev.managemoneycommandservice.aggregates;

import com.kaly7dev.coreapi.CreateTransactionCommand;
import com.kaly7dev.coreapi.TransactionCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Slf4j
public class TransactionAggregate {
    @AggregateIdentifier
    private String transId;
    private String desc;
    private Double amount;
    private String month;
    private String weekNum;

    public TransactionAggregate(){
    }

    @CommandHandler
    public TransactionAggregate(CreateTransactionCommand command){
        log.info("CreateTransactionCommand received");
        AggregateLifecycle.apply(new TransactionCreatedEvent(
                command.getId(),
                command.getDesc(),
                command.getAmount(),
                command.getMonth(),
                command.getWeek()
        ));
    }

    @EventSourcingHandler
    public void on( TransactionCreatedEvent event){
        log.info("TransactionCreatedEvent occured");
        this.transId= event.getId();
        this.desc= event.getDesc();
        this.amount= event.getAmount();
        this.month= event.getMonth();
        this.weekNum= event.getWeek();
    }
}
