package com.CBHub.wrapper.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class GlobalExceptionHandler {

    private ResponseEntity<Object> build(HttpStatus status, String message) {

        return ResponseEntity.status(status).body(
                Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", status.value(),
                        "error", status.getReasonPhrase(),
                        "message", message
                )

        );


        //H

    }


}
