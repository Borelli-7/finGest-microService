package com.kaly7dev.managemoneycommandservice.controllers;

import com.kaly7dev.coreapi.CreateTransactionCommand;
import com.kaly7dev.coreapi.TransactionResqDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/transaction/commands")
public class TransactionCommandController {
    private CommandGateway commandGateway;
    private EventStore eventStore;

    @PostMapping("/create")
    public CompletableFuture<String> newTransaction(@RequestBody TransactionResqDto resqDto){
        CompletableFuture<String> response = commandGateway.send(new CreateTransactionCommand(
                UUID.randomUUID().toString(),
                resqDto.getDesc(),
                resqDto.getAmount(),
                resqDto.getMonth(),
                resqDto.getWeek()
        ));
        return response;
    }

    @GetMapping("/eventStore/{transId}")
    public Stream eventStore(@PathVariable String transId){
        return eventStore.readEvents(transId).asStream();
    }
}
