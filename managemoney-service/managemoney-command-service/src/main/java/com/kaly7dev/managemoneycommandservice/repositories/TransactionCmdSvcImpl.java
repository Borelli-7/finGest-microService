package com.kaly7dev.managemoneycommandservice.repositories;

import com.kaly7dev.coreapi.TransactionResqDto;

import java.util.concurrent.CompletableFuture;

public class TransactionCmdSvcImpl implements TransactionCmdRepo {
    @Override
    public CompletableFuture<String> createTransaction(TransactionResqDto resqDto) {
        return null;
    }
}
