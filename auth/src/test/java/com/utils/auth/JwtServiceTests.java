package com.utils.auth;

import com.utils.auth.exception.InvalidTokenException;
import com.utils.auth.model.UserStatus;
import com.utils.auth.service.JwtService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtServiceTests {

    @Autowired
    JwtService jwtService;

    @Test
    public void testValidTokenSuccess(){
        UserDetails userDetails = new UserStatus("user",true);
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token,userDetails));

    }
    @Test
    public void testInvalidTokenFail(){
        UserDetails userDetails = new UserStatus("user",true);
        assertThrows(InvalidTokenException.class,()->jwtService.isTokenValid("token",userDetails));
    }
    @Test
    public void testExpiredTokenFail(){
        UserDetails userDetails = new UserStatus("h.gamil@sumerge.com",true);
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoLmdhbWlsQHN1bWVyZ2UuY29tIiwiaWF0IjoxNzA2ODk4MzA0LCJleHAiOjE3MDY4OTg0MjR9.LeeLHdkofg578lVGz4_xN6HUsIGB5Z7m4t94-NaJrsI";
        assertThrows(InvalidTokenException.class,()->jwtService.isTokenValid("token",userDetails));

    }
    @Test
    public void testGenerateAccessTokenSuccess(){
        UserDetails userDetails = new UserStatus("user",true);
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);

    }
    @Test
    public void testGenerateRefreshTokenSuccess(){
        UserDetails userDetails = new UserStatus("user",true);
        String token = jwtService.generateRefreshToken(userDetails);
        assertNotNull(token);
    }
}
