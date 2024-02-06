package com.utils.auth.exception;

public class NoTokenProvidedException extends RuntimeException {
    public NoTokenProvidedException(){
        super();
    }
    public NoTokenProvidedException(String message){
        super(message);
    }
}
