package com.cft.focusstart.library.controller;

import com.cft.focusstart.library.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex, WebRequest request) {
        return new ResponseEntity<>(
                ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<String> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_IMPLEMENTED
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<String>httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex, WebRequest request) {
        return new ResponseEntity<>(
                ex.getMessage(),
                BAD_REQUEST
        );
    }

    @ExceptionHandler(ServiceException.class)
    protected ResponseEntity<String> serviceExceptionHandler(ServiceException ex) {
        return new ResponseEntity<>(
                ex.getMessage(),
                BAD_REQUEST
        );
    }
}
