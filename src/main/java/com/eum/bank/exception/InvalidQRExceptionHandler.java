package com.eum.bank.exception;

public class InvalidQRExceptionHandler extends RuntimeException{

    public InvalidQRExceptionHandler(String message) {
        super(message);
    }
}
