package com.kaly7dev.accounts.exceptions;

public class AccountServiceException extends RuntimeException{
    public AccountServiceException(String exMessage){
        super(exMessage);
    }
    public AccountServiceException(String exMessage, Exception exception){
        super(exMessage, exception);
    }
}
