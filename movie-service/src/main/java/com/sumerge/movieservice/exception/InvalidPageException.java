package com.sumerge.movieservice.exception;

import com.utils.auth.exception.InvalidTokenException;

public class InvalidPageException extends RuntimeException{
    public InvalidPageException(){
        super();
    }
    public InvalidPageException(String message){
        super(message);
    }
}
