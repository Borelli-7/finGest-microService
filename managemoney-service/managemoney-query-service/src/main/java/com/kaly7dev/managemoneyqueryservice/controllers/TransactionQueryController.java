package com.kaly7dev.managemoneyqueryservice.controllers;

import com.kaly7dev.coreapi.GetAllTransactionQuery;
import com.kaly7dev.coreapi.GetTransactionByIdQuery;
import com.kaly7dev.managemoneyqueryservice.entities.Transaction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/transaction/query")
public class TransactionQueryController {
    private QueryGateway queryGateway;

    @GetMapping("/all")
    public CompletableFuture<List<Transaction>>transactions(){
        return queryGateway.query(new GetAllTransactionQuery(),
                ResponseTypes.multipleInstancesOf(Transaction.class));
    }

    @GetMapping("/byid/{id}")
    public CompletableFuture<Transaction> getTransaction(@PathVariable String id){
        return queryGateway.query(new GetTransactionByIdQuery(id),
                ResponseTypes.instanceOf(Transaction.class));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
    }

}
