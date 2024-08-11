package com.eum.bank.exception;

/**
 * HMAC 해싱 값이 서명 값과 다를 경우.
 */
public class HmacVerificationFailedException extends RuntimeException{

    public HmacVerificationFailedException(String message) {
        super(message);
    }
}
