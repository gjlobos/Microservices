package com.dh.userservice.shared.handler.exception;

public class ApiRateLimitException extends RuntimeException{
    
    public ApiRateLimitException(String message){
        super(message);
    }
}
