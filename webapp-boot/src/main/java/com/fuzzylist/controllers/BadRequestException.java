package com.fuzzylist.controllers;

import com.fuzzylist.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends ApplicationException {

    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }
}
