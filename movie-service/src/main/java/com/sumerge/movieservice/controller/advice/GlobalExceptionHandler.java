package com.sumerge.movieservice.controller.advice;

import com.sumerge.movieservice.exception.InvalidPageException;
import com.sumerge.movieservice.exception.MovieNotFoundException;
import com.sumerge.movieservice.model.dto.Response;
import com.utils.auth.exception.UserAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<Response> handleMovieNotFoundException(MovieNotFoundException ex) {
        Response response = new Response(false,"Movie Not found",null);
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(InvalidPageException.class)
    public ResponseEntity<Response> handleMovieNotFoundException(InvalidPageException ex) {
        Response response = new Response(false,"Invalid Page Request",null);
        return ResponseEntity.badRequest().body(response);
    }

}
