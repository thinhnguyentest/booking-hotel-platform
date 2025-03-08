package com.booking_hotel.api.exception;


import com.booking_hotel.api.utils.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .path(request.getContextPath())
                .timestamp(DateUtils.now("dd/MM/yyyy'T'HH:mm:ss"))
                .errors(Collections.singletonList(ex.getMessage()))
                .build();
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }
}


