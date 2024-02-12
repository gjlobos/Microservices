package com.dh.userservice.shared.handler.exception;

public class UserUnauthorizedException extends RuntimeException{
    
    public UserUnauthorizedException(String message){
        super(message);
    }
}
