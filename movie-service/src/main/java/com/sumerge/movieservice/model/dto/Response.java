package com.sumerge.movieservice.model.dto;

import com.sumerge.movieservice.model.repository.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class Response {
    private boolean success;
    private String message;
    private List<Movie> data;

    public static  Response success(List<Movie> data) {
        return new Response(true, "RequestSuccessful", data);
    }
    public static Response error(String message) {
        return new Response(false, message, null);
    }
}
