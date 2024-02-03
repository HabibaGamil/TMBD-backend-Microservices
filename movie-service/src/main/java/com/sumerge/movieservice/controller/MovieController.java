package com.sumerge.movieservice.controller;


import com.sumerge.movieservice.model.dto.Response;
import com.sumerge.movieservice.exception.MovieNotFoundException;
import com.sumerge.movieservice.model.repository.Movie;
import com.sumerge.movieservice.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
public class MovieController {

    @Autowired
    MovieService movieService;

    @GetMapping("/")
    public String hello(){
        return "Hello from movie service";

    }
    @GetMapping("/movie/{id}")
    public ResponseEntity<Response> getMovie(@PathVariable String id){
        try{
           Movie movie = movieService.getMovie(id);
            List<Movie> resData = new ArrayList<>();
            resData.add(movie);
           return ResponseEntity.ok(Response.success(resData));

        } catch(MovieNotFoundException exception){
             return ResponseEntity.badRequest().body(Response.error("Movie Not found"));
        }

    }
    @GetMapping("/movies")
    public ResponseEntity<Response> getMoviePage(@RequestParam Map<String, String> queryParams) {

        int page = queryParams.containsKey("page") ? Integer.parseInt(queryParams.get("page")) : 0;

        String sortBy = queryParams.getOrDefault("sortBy", "voteAverage");
        String sortOrder = queryParams.getOrDefault("sortOrder", "desc");

        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        PageRequest pageable = PageRequest.of(page, 20, sort);

        List<Movie> movies = movieService.getMoviePage(pageable);
        if(movies.size()==0){
            return ResponseEntity.badRequest().body(Response.error("Invalid Page Request"));
        }
        return ResponseEntity.ok(Response.success(movies));

    }
//    @PostMapping("/populate")
//    public String populateMoviesDatabase(){
//        movieService.populateDatabase();
//        return "database populated";
//    }

}
