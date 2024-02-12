package com.dh.transactionservice.shared.handler.exception;

public class UserUnauthorizedException extends RuntimeException{
    
    public UserUnauthorizedException(String message){
        super(message);
    }
}
