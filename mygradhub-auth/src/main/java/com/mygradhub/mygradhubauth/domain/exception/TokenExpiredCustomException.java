package com.mygradhub.mygradhubauth.domain.exception;

public class TokenExpiredCustomException extends RuntimeException {
    public TokenExpiredCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
