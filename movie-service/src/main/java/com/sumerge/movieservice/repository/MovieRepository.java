package com.sumerge.movieservice.repository;

import com.sumerge.movieservice.model.repository.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  MovieRepository extends MongoRepository<Movie, String> {
    Optional<Movie> findMovieById(String id);
    Page<Movie> findAll(Pageable pageable);

}
