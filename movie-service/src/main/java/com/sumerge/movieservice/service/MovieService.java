package com.sumerge.movieservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumerge.movieservice.exception.InvalidPageException;
import com.sumerge.movieservice.exception.MovieNotFoundException;
import com.sumerge.movieservice.model.repository.Movie;
import com.sumerge.movieservice.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    public Movie getMovie(String id) throws MovieNotFoundException{
        Optional<Movie> movieOptional = movieRepository.findMovieById(id);
        if(movieOptional.isEmpty()){
            throw new MovieNotFoundException("No Such Movie");
        }
        return movieOptional.get();
    }
    public List<Movie> getMoviePage(PageRequest request) throws InvalidPageException{
         Page<Movie> page = movieRepository.findAll(request);
         if(page.getContent().size()==0){
             throw new InvalidPageException();
         }
         return page.getContent();
    }


//    public void populateDatabase(){
//        try{
//            Resource resource = new ClassPathResource("data.json");
//            File file = resource.getFile();
//            ObjectMapper objectMapper = new ObjectMapper();
//            List<Movie> movies = Arrays.asList(objectMapper.readValue(file, Movie[].class));
//            movieRepository.saveAll(movies);
//        }catch(Exception exc){
//            exc.printStackTrace();
//        }
//    }

}
