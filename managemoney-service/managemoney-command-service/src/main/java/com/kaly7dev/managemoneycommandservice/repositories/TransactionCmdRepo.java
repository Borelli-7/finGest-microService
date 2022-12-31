package com.kaly7dev.managemoneycommandservice.repositories;

import com.kaly7dev.coreapi.TransactionResqDto;

import java.util.concurrent.CompletableFuture;

public interface TransactionCmdRepo {
    CompletableFuture<String> createTransaction( TransactionResqDto resqDto);
}
