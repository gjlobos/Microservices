package com.dh.accountservice.shared.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class ResourceNotContent extends RuntimeException {

    public ResourceNotContent() {
        super();
    }

}
