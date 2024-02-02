package com.sumerge.movieservice.controller;

import com.sumerge.movieservice.TestSetup;
import com.sumerge.movieservice.model.repository.Movie;
import com.sumerge.movieservice.repository.MovieRepository;
import com.sumerge.movieservice.service.MovieService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MovieControllerTests  extends TestSetup {
    @Test
    public void testGetMovieSuccess() throws Exception {

        mockMvc.perform(get("/movie/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("RequestSuccessful"))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data[0]").exists());
    }
    @Test
    public void testGetMovieFail() throws Exception {

        mockMvc.perform(get("/movie/2"))

               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.success").value("false"))
               .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.message").value("Movie Not found"))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetMoviePage() throws Exception {

        mockMvc.perform(get("/movies")
                        .param("page", "0")
                        .param("sortBy", "voteAverage")
                        .param("sortOrder", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").exists());

    }

    @Test
    public void testGetMoviePageInvalidRequest() throws Exception {
        mockMvc.perform(get("/movies")
                .param("page", "1")
                .param("sortBy", "voteAverage")
                .param("sortOrder", "desc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.message").value("Invalid Page Request"));
    }
}

