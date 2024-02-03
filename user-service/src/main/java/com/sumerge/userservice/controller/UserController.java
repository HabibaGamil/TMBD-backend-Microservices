package com.sumerge.userservice.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumerge.userservice.model.dto.Response;
import com.sumerge.userservice.model.dto.UserDto;
import com.utils.auth.exception.UserAlreadyExistsException;
import com.sumerge.userservice.service.UserService;
import com.utils.auth.exception.UserNotFoundException;
import com.utils.auth.service.AuthService;
import com.utils.auth.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController
@Validated
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    @PostMapping("user/signup")
    public ResponseEntity<Response> signup( @Valid @RequestBody UserDto userDto){
        Response response;
        try{
          response = userService.addUser(userDto);
          return ResponseEntity.ok(response);
        }
        catch (UserAlreadyExistsException exception){
          return ResponseEntity.badRequest().body(new Response("A user with this email already exists"));
        }
    }
    @PostMapping("user/login")
    public ResponseEntity<Response> login(@RequestBody UserDto userDto){
        Response response;
        try{
           response = userService.login(userDto);
           return ResponseEntity.ok(response);
        }
        catch (UserNotFoundException exception){
             return ResponseEntity.badRequest().body(new Response("Invalid Login Credentials"));
        }

    }
    @PostMapping("user/logout")
    public ResponseEntity<Response> logout(HttpServletRequest request,@RequestBody String requestBody){
        String refreshToken = extractRefreshToken(request.getCookies(),requestBody);
        if(refreshToken==null){
            return ResponseEntity.badRequest().body(new Response("No token provided"));
        }
        try{
            Response response = userService.logout(refreshToken);
            return  ResponseEntity.ok(response);
        }catch(Exception exc){
            exc.printStackTrace();
            return ResponseEntity.badRequest().body(new Response("Bad Request"));
        }
    }

    @PostMapping("user/refresh")
    public ResponseEntity<Response> getRefreshToken(HttpServletRequest request, @RequestBody String requestBody){

        //Retrieving refresh cookie from request
        String refreshToken = extractRefreshToken(request.getCookies(),requestBody);
        if(refreshToken==null){
            return ResponseEntity.badRequest().body(new Response("No token provided"));
        }
        try {
            Response res = userService.refresh(refreshToken);
            return ResponseEntity.ok(res);

        } catch (Exception exception){
            return ResponseEntity.badRequest().body(new Response("Invalid Refresh Token"));
        }

    }
    private String extractRefreshToken(Cookie[] cookies, String requestBody)  {
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken= cookie.getValue();
                }
            }
        }
        if(refreshToken==null){
            JsonNode jsonNode = null;
            try {
                jsonNode = objectMapper.readTree(requestBody);
                return jsonNode.get("refreshToken").asText();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
