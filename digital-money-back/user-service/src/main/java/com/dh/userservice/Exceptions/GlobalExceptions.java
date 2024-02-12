package com.dh.userservice.Exceptions;

import com.dh.userservice.domain.dto.response.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptions {
    @ExceptionHandler({ResourceNotFoundException.class})
    public ApiResponseDto procesarErrorNotFound(ResourceNotFoundException ex) {
        log.error(ex.getMessage());
        return new ApiResponseDto(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
    }

    @ExceptionHandler({BadRequestException.class})
    public ApiResponseDto procesarErrorBadRequest(BadRequestException ex) {
        log.error(ex.getMessage());
        return new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
    }
}
