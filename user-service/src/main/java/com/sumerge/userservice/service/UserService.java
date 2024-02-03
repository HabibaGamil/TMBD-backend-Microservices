package com.sumerge.userservice.service;

import com.sumerge.userservice.mapper.Mapper;
import com.sumerge.userservice.model.dto.Response;
import com.sumerge.userservice.model.dto.UserDto;
import com.utils.auth.exception.InvalidTokenException;
import com.utils.auth.exception.UserAlreadyExistsException;
import com.sumerge.userservice.model.entity.User;
import com.sumerge.userservice.repository.UserRepository;

import com.utils.auth.exception.UserNotFoundException;
import com.utils.auth.model.UserStatus;
import com.utils.auth.service.AuthService;
import com.utils.auth.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository repo;
    @Autowired
    Mapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthService authService;

    public Response addUser(UserDto userDto) {
        //Check if user with such email exists
        Optional<User> userOptional = Optional.ofNullable(repo.findByEmail(userDto.getEmail()));
        if(userOptional.isPresent()){
            throw new UserAlreadyExistsException();
        }
        User user = mapper.toUser(userDto);
        //encrypt password before saving new user
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        repo.save(user);

        authService.setUserStatus(user.getEmail(),true);
        return createResponse(user,"Registration Successful");
    }
    public Response login(UserDto userDto) {

        //check if user with such email exists
        Optional<User> userOptional = Optional.ofNullable(repo.findByEmail(userDto.getEmail()));
        if(userOptional.isEmpty()){
            throw new UserNotFoundException();
        }
        //check if input password matches stored password
        boolean passwordMatch = passwordEncoder.matches(userDto.getPassword(), userOptional.get().getPassword());
        if(!passwordMatch){
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        user.setIsLoggedIn(true);
        repo.save(user);
        authService.setUserStatus(user.getEmail(),true);
        return createResponse(user,"Login Successful");


    }
    public Response loginByEmail(String email, String refreshToken) throws InvalidTokenException {

        //check if user with such email exists
        Optional<User> userOptional = Optional.ofNullable(repo.findByEmail(email));
        if(userOptional.isEmpty()){
            throw new InvalidTokenException("Invalid Refresh Token");
        }
        User user = userOptional.get();
        UserStatus userStatus = new UserStatus(user.getEmail(),true);
        authService.setUserStatus(user.getEmail(),true);
        String accessToken = jwtService.generateToken(userStatus);
        return new Response(accessToken,refreshToken,"Login Successful!");

    }

    public Response logout(String refreshToken){
        //check if user with such email exists
        String email = jwtService.extractUsername(refreshToken);

        Optional<User> userOptional = Optional.ofNullable(repo.findByEmail(email));
        if(userOptional.isEmpty()){
            throw new UserNotFoundException();
        }
        User user= userOptional.get();
        user.setIsLoggedIn(false);
        repo.save(user);
        authService.setUserStatus(user.getEmail(),false);
        return new Response("Logout successful!");

    }

    public Response refresh(String refreshToken) throws InvalidTokenException{
        boolean expired = jwtService.isTokenExpired(refreshToken);
        String email = jwtService.extractUsername(refreshToken);

        if(expired){
            this.logout(refreshToken);
            throw new InvalidTokenException();
        }
        return this.loginByEmail(email,refreshToken);

    }

    private Response createResponse(User user, String message){
        UserStatus userStatus = new UserStatus(user.getEmail(),true);
        String accesToken = jwtService.generateToken(userStatus);
        String refreshToken = jwtService.generateRefreshToken(userStatus);
        return new Response(accesToken,refreshToken,message);

    }

}
