package com.CBHub.wrapper.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Invalid ID") //404
public class ComicNotFoundException extends RuntimeException {
}
