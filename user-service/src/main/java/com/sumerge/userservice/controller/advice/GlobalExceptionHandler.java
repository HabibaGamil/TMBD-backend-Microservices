package com.sumerge.userservice.controller.advice;

import com.sumerge.userservice.model.dto.ErrorResponse;
import com.utils.auth.exception.InvalidTokenException;
import com.utils.auth.exception.NoTokenProvidedException;
import com.utils.auth.exception.UserAlreadyExistsException;
import com.utils.auth.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ErrorResponse response = new ErrorResponse(true,"A user with this email already exists");
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(true,"Invalid Login Credentials");
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(NoTokenProvidedException.class)
    public ResponseEntity<ErrorResponse> handleNoTokenProvidedException(NoTokenProvidedException ex) {
        ErrorResponse response = new ErrorResponse(true,"No token provided");
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException ex) {
        ErrorResponse response = new ErrorResponse(true,"Invalid Refresh Token");
        return ResponseEntity.badRequest().body(response);
    }


}
