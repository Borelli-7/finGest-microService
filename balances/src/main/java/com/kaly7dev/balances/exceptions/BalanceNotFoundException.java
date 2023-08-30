package com.kaly7dev.balances.exceptions;

public class BalanceNotFoundException extends RuntimeException{
    public BalanceNotFoundException(String exMessage, Exception exception){
        super(exMessage, exception);
    }
    public BalanceNotFoundException(String exMessage){
        super(exMessage);
    }
}
