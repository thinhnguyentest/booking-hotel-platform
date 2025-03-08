package com.booking_hotel.api.exception;


import com.booking_hotel.api.utils.dateUtils.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .path(request.getDescription(false))
                .timestamp(DateUtils.now("dd/MM/yyyy'T'HH:mm:ss"))
                .errors(Collections.singletonList(ex.getMessage()))
                .build();

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .path(request.getDescription(false))
                .timestamp(DateUtils.now("dd/MM/yyyy'T'HH:mm:ss"))
                .errors(Collections.singletonList(ex.getMessage()))
                .build();
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    // Xử lý NullPointerException (Ví dụ: Giá trị null không mong muốn)
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiErrorResponse> handleNullPointerException(NullPointerException ex, WebRequest request) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .path(request.getDescription(false))
                .timestamp(DateUtils.now("dd/MM/yyyy'T'HH:mm:ss"))
                .errors(Collections.singletonList("Unexpected null value"))
                .build();
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Xử lý ResourceNotFoundException (Ví dụ: Không tìm thấy dữ liệu)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .path(request.getDescription(false))
                .timestamp(DateUtils.now("dd/MM/yyyy'T'HH:mm:ss"))
                .errors(Collections.singletonList(ex.getMessage()))
                .build();
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    // Xử lý AccessDeniedException (Ví dụ: Không có quyền truy cập)
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(org.springframework.security.access.AccessDeniedException ex, WebRequest request) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .path(request.getDescription(false))
                .timestamp(DateUtils.now("dd/MM/yyyy'T'HH:mm:ss"))
                .errors(Collections.singletonList("Access Denied"))
                .build();
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.FORBIDDEN);
    }

    // Xử lý MethodArgumentNotValidException (Lỗi validation dữ liệu đầu vào)
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(org.springframework.web.bind.MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .path(request.getDescription(false))
                .timestamp(DateUtils.now("dd/MM/yyyy'T'HH:mm:ss"))
                .errors(errors)
                .build();
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(ElementNotFoundException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .path(request.getDescription(false))
                .timestamp(DateUtils.now("dd/MM/yyyy'T'HH:mm:ss"))
                .errors(errors)
                .build();
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }
}


