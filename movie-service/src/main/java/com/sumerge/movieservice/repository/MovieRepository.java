package com.sumerge.movieservice.repository;

import com.sumerge.movieservice.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface  MovieRepository extends MongoRepository<Movie, String> {
    Optional<Movie> findMovieById(String id);
    Page<Movie> findAll(Pageable pageable);

}
