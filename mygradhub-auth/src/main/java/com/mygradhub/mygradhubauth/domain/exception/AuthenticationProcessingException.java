package com.mygradhub.mygradhubauth.domain.exception;

public class AuthenticationProcessingException extends RuntimeException {

    public AuthenticationProcessingException(String message, Throwable e) {
        super(message, e);
    }
}
