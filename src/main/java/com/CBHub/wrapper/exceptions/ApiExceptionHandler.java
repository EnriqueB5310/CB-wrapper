package com.CBHub.wrapper.exceptions;

public class ApiExceptionHandler extends RuntimeException {
        public ApiExceptionHandler(String message) {
            super(message);
        }

        public ApiExceptionHandler(String message, Throwable cause) {
            super(message, cause);
        }

}
