package com.eum.bank.exception;

public class QRCodeExpiredException extends RuntimeException {

    public QRCodeExpiredException(String message) {
        super(message);
    }
}
