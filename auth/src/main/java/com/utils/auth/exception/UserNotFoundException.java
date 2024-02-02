package com.utils.auth.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(){
        super();
    }
    public UserNotFoundException(final String message){
        super(message);
    }
}
