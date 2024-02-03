package com.sumerge.userservice.service;

import com.sumerge.userservice.TestSetup;
import com.sumerge.userservice.model.dto.Response;
import com.sumerge.userservice.model.dto.UserDto;
import com.sumerge.userservice.model.entity.User;
import com.sumerge.userservice.repository.UserRepository;
import com.utils.auth.exception.InvalidTokenException;
import com.utils.auth.exception.UserAlreadyExistsException;
import com.utils.auth.exception.UserNotFoundException;
import com.utils.auth.service.AuthService;
import com.utils.auth.service.JwtService;
import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests extends TestSetup {

    @Autowired
    UserService userService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    AuthService authService;
    @MockBean
    UserRepository userRepository;
    @Before
    public void dependenciesSetup(){
        User user = new User(1,"user","user.1@sumerge.com","User123",true);
        Mockito.when(userRepository.findByEmail("user.1@sumerge.com")).thenReturn(user);
        Mockito.when(userRepository.findByEmail("user.2@sumerge.com")).thenReturn(null);
        Mockito.when(jwtService.extractUsername("token1")).thenReturn("user.1@sumerge.com");
        Mockito.when(jwtService.extractUsername("token2")).thenReturn("user.2@sumerge.com");
        Mockito.when(jwtService.extractUsername("token3")).thenReturn("user.1@sumerge.com");
        Mockito.when(jwtService.isTokenExpired("token1")).thenReturn(false);
        Mockito.when(jwtService.isTokenExpired("token2")).thenReturn(false);
        Mockito.when(jwtService.isTokenExpired("token3")).thenReturn(true);

    }
    @Test
    public void testSignupSuccess(){
        UserDto userDto = new UserDto(null,"user.2@sumerge.com","User123",null);
        Response response = userService.addUser(userDto);
        assertNotNull(response);

    }
    @Test
    public void testSignupFail(){
        UserDto userDto = new UserDto(null,"user.1@sumerge.com","User123",null);
        assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(userDto));

    }
    @Test
    public void testLoginSuccess(){
        UserDto userDto = new UserDto(null,"user.2@sumerge.com","User123",null);
        Response response = userService.addUser(userDto);
        assertNotNull(response);

    }
    @Test
    public void testLoginFail(){
        UserDto userDto = new UserDto(null,"user.2@sumerge.com","User123",null);
        assertThrows(UserNotFoundException.class, () -> userService.login(userDto));

    }
    @Test
    public void testLogoutSuccess(){

        Response response = userService.logout("token1");
        assertNotNull(response);

    }
    @Test
    public void testLogoutFail(){
        assertThrows(UserNotFoundException.class, () -> userService.logout("token2"));

    }
    @Test
    public void testRefreshSuccess(){
        Response response = userService.refresh("token1");
        assertNotNull(response);

    }
    @Test
    public void testRefreshExpiredFail(){
        assertThrows(InvalidTokenException.class, () -> userService.refresh("token3"));

    }
    @Test
    public void testRefreshUserNotFoundFail(){
        assertThrows(InvalidTokenException.class, () -> userService.refresh("token2"));

    }




}
