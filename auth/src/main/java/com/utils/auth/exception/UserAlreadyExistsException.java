package com.utils.auth.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException() {
        super();
    }
    public UserAlreadyExistsException(final String message) {
        super(message);
    }
}
