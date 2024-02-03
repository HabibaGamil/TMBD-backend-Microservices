package com.sumerge.userservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sumerge.userservice.TestSetup;
import com.sumerge.userservice.model.dto.Response;
import com.sumerge.userservice.model.dto.UserDto;
import com.sumerge.userservice.service.UserService;
import com.utils.auth.exception.InvalidTokenException;
import com.utils.auth.exception.UserAlreadyExistsException;
import com.utils.auth.exception.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTests extends TestSetup {
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Before
    public void serviceSetup(){
        UserDto userDto1 = new UserDto("User1","user.1@sumerge.com","User1234","User1234");
        UserDto userDto2 = new UserDto("User2","user.2@sumerge.com","User1234","User1234");
        UserDto userDto3 = new UserDto(null,"user.1@sumerge.com","User1234",null);
        UserDto userDto4 = new UserDto(null,"user.2@sumerge.com","User1234",null);
        Mockito.when(userService.addUser(userDto1)).thenReturn(new Response("signup success"));
        Mockito.when(userService.addUser(userDto2)).thenThrow(new UserAlreadyExistsException());
        Mockito.when(userService.login(userDto3)).thenReturn(new Response("Login Successful"));
        Mockito.when(userService.login(userDto4)).thenThrow(new UserNotFoundException());
        Mockito.when(userService.logout("token")).thenReturn(new Response("Logout Success"));
        Mockito.when(userService.refresh("validToken")).thenReturn(new Response("Login Success"));
        Mockito.when(userService.refresh("invalidToken")).thenThrow(new InvalidTokenException());
    }
    @Test
    public void testUserSignupSuccess() throws Exception {
        UserDto userDto = new UserDto("User1","user.1@sumerge.com","User1234","User1234");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto));
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testUserSignupInvalidPasswordFail() throws Exception {
        UserDto userDto = new UserDto("User1","user.1@sumerge.com","user1234","user1234");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

    }
    @Test
    public void testUserSignupInvalidEmailFail() throws Exception {
        UserDto userDto = new UserDto("User1","sumerge.com","User1234","User1234");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

    }
    @Test
    public void testUserSignupAlreadyExistsFail() throws Exception {
        UserDto userDto= new UserDto("User2","user.2@sumerge.com","User1234","User1234");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("A user with this email already exists"))
                .andDo(MockMvcResultHandlers.print());


    }
    @Test
    public void testUserLoginSuccess() throws Exception {
        UserDto userDto = new UserDto(null,"user.1@sumerge.com","User1234",null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                 .andExpect(jsonPath("$.message").value("Login Successful"))
                .andDo(MockMvcResultHandlers.print());

    }
    @Test
    public void testUserLoginFail() throws Exception {
        UserDto userDto = new UserDto(null,"user.2@sumerge.com","User1234",null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Login Credentials"))
                .andDo(MockMvcResultHandlers.print());

    }
    @Test
    public void testUserLogoutSuccess() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("refreshToken", "token")));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout Success"))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testUserLogoutNoTokenFail() throws Exception {
        ObjectNode emptyJsonNode = objectMapper.createObjectNode();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyJsonNode));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No token provided"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void testUserRefreshSuccess() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("refreshToken", "validToken")));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login Success"))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testUserNoTokenFail() throws Exception {
        ObjectNode emptyJsonNode = objectMapper.createObjectNode();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyJsonNode));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No token provided"))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testUserInvalidTokenFail() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("refreshToken", "invalidToken")));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Refresh Token"))
                .andDo(MockMvcResultHandlers.print());
    }


}
