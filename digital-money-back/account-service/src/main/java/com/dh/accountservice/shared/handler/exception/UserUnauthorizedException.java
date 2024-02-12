package com.dh.accountservice.shared.handler.exception;

public class UserUnauthorizedException extends RuntimeException{
    
    public UserUnauthorizedException(String message){
        super(message);
    }
}
