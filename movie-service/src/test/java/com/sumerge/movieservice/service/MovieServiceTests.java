package com.sumerge.movieservice.service;

import com.sumerge.movieservice.TestSetup;
import com.sumerge.movieservice.exception.MovieNotFoundException;
import com.sumerge.movieservice.model.repository.Movie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MovieServiceTests extends TestSetup {
    @Autowired
    MovieService movieService;
    @Test
    public void testGetMovie() throws MovieNotFoundException {
        Movie result = movieService.getMovie("1");
        assertNotNull(result);

    }

    @Test
    public void testGetMovieNotFound() {
        assertThrows(MovieNotFoundException.class, () -> movieService.getMovie("2"));
    }

    @Test
    public void testGetMoviePage() {

        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by("voteAverage").descending());
        List<Movie> result = movieService.getMoviePage(pageRequest);
        assertNotNull(result);

    }


}
