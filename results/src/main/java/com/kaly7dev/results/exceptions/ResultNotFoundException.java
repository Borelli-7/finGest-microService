package com.kaly7dev.results.exceptions;

public class ResultNotFoundException extends RuntimeException{
    public ResultNotFoundException(String exMessage, Exception exception){
        super(exMessage, exception);
    }

    public ResultNotFoundException(String exMessage){
        super(exMessage);
    }
}
