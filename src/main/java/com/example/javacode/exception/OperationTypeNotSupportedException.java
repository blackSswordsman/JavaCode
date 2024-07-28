package com.example.javacode.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Operation type is not supported")
public class OperationTypeNotSupportedException extends ApplicationException{
    public OperationTypeNotSupportedException() {
       super("Operation type is not supported");
    }
}
