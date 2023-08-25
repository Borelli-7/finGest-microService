package com.kaly7dev.results.exceptions;

public class ResultServiceException extends RuntimeException{
    public ResultServiceException(String exMessage, Exception exception){
        super(exMessage, exception);
    }
    public ResultServiceException(String exMessage){
        super(exMessage);
    }
}
