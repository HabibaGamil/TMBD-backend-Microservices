package com.sumerge.movieservice.auth;

import com.sumerge.movieservice.controller.MovieController;
import com.sumerge.movieservice.service.MovieService;
import com.utils.auth.model.UserStatus;
import com.utils.auth.service.AuthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringRunner.class)
//@WebMvcTest(MovieController.class)
@SpringBootTest
public class AuthFilterTest  {

    private MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    MovieController movieController;
    @MockBean
    MovieService movieService;
    @MockBean
    AuthService authService;
    @Before
    public void authServiceSetup(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        UserDetails userDetails = new UserStatus("testUser",true);
        Mockito.when(authService.isAuthenticatedUser("validToken")).thenReturn(userDetails);
        Mockito.when(authService.isAuthenticatedUser("invalidToken")).thenReturn(null);
        //Mockito.when(authService.findByEmail("testUser")).thenReturn(userDetails);
    }

    @Test
    public void testFilterWithValidToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/movie/1")
                       .header("Authorization", "Bearer " + "validToken"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testFilterWithoutToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/movie/1"))
                      .andExpect(MockMvcResultMatchers.status().isForbidden())
                      .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testFilterWithInvalidToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/movie/1")
                        .header("Authorization", "Bearer " + "InvalidToken"))
                        .andExpect(MockMvcResultMatchers.status().isForbidden());
    }



}
