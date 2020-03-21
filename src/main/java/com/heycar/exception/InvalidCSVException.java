package com.heycar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidCSVException extends CodeDefinedException {

    public InvalidCSVException(String message) {
        super(message);
    }
}
