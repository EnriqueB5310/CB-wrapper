package com.CBHub.wrapper.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class DefaultHandler {
    @ExceptionHandler(value = {ApiExceptionHandler.class})
    public ResponseEntity<Object> handleBadUrl(ApiExceptionHandler e) {
        //create payload containing exception details then return entity
         ApiException apiException = new ApiException(e.getMessage(), e, HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));


        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

}
