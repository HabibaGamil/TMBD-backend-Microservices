package com.sumerge.movieservice;

import com.sumerge.movieservice.model.repository.Movie;
import com.sumerge.movieservice.repository.MovieRepository;
import com.sumerge.movieservice.service.MovieService;
import com.utils.auth.model.UserStatus;
import com.utils.auth.service.AuthService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TestSetup {
    public  MockMvc mockMvc;
    @Autowired
     WebApplicationContext webApplicationContext;

    @MockBean
    MovieRepository movieRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        movieRepositorySetup();


    }
    void movieRepositorySetup(){
        //Mock data
        Movie mockMovie = new Movie();
        PageRequest pageable1 = PageRequest.of(0, 20, Sort.by("voteAverage").descending());
        PageRequest pageable2 = PageRequest.of(1, 20, Sort.by("voteAverage").descending());
        List<Movie> mockMovies = new ArrayList<>();
        mockMovies.add(mockMovie);
        //Mock repository
        Mockito.when(movieRepository.findMovieById("1")).thenReturn(Optional.of(mockMovie));
        Mockito.when(movieRepository.findMovieById("2")).thenReturn(Optional.empty());
        Mockito.when(movieRepository.findAll(pageable1)).thenReturn(new PageImpl<>(mockMovies));
        Mockito.when(movieRepository.findAll(pageable2)).thenReturn(new PageImpl<>(Collections.emptyList()));

    }


}
