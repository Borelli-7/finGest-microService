package com.kaly7dev.managemoneycommandservice.controllers;

import com.kaly7dev.coreapi.CreateTransactionCommand;
import com.kaly7dev.coreapi.TransactionResqDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RefreshScope
@Slf4j
@AllArgsConstructor
@RequestMapping("/transaction/commands")
public class TransactionCommandController {
    private CommandGateway commandGateway;
    private EventStore eventStore;

/*
    private String serverPort;
    private String value;

    @Autowired
    public TransactionCommandController(@Value("${server.port}") String serverPort, @Value("${value}") String value ) {
        this.serverPort = serverPort;
        this.value = value;
    }*/


    @PostMapping("/create")
    public CompletableFuture<String> newTransaction(@RequestBody TransactionResqDto resqDto){
        return commandGateway.send(new CreateTransactionCommand(
                UUID.randomUUID().toString(),
                resqDto.getDesc(),
                resqDto.getAmount(),
                resqDto.getMonth(),
                resqDto.getWeek()
        ));
    }

    @GetMapping("/eventStore/{transId}")
    public Stream eventStore(@PathVariable String transId){
        return eventStore.readEvents(transId).asStream();
    }



/*    @GetMapping("/myconfig")
    public Map<String, Object> myConfig(){
        Map<String, Object> params = new HashMap< >();
        params.put("server.port", serverPort);
        params.put("value", value);
        params.put("threadName", Thread.currentThread().getName());
        return params;
    }*/
}
