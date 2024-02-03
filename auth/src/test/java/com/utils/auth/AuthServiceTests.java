package com.utils.auth;

import com.utils.auth.model.UserStatus;
import com.utils.auth.repository.UserStatusRepository;
import com.utils.auth.service.AuthService;
import com.utils.auth.service.JwtService;
import com.utils.auth.service.UserAuthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthServiceTests {

    @MockBean
    UserStatusRepository userStatusRepository;

    @Autowired
    AuthService authService;

    @Autowired
    UserAuthService userAuthService;

    @MockBean
    JwtService jwtService;

    @Before
    public void repositorySetup(){
        Mockito.when(userStatusRepository.findUserStatusByEmail("user1@sumerge.com"))
                .thenReturn(Optional.of(new UserStatus("user1@sumerge.com", true)));
        Mockito.when(userStatusRepository.findUserStatusByEmail("user2@sumerge.com"))
                .thenReturn(Optional.of(new UserStatus("user2@sumerge.com", false)));
        Mockito.when(jwtService.isTokenExpired("token1")).thenReturn(false);
        Mockito.when(jwtService.extractUsername("token1")).thenReturn("user1@sumerge.com");
        Mockito.when(jwtService.isTokenExpired("token2")).thenReturn(true);
        Mockito.when(jwtService.isTokenExpired("token3")).thenReturn(false);
        Mockito.when(jwtService.extractUsername("token2")).thenReturn("user2@sumerge.com");

    }
    @Test
    public void testSetUserStatusSuccess(){
        Assertions.assertDoesNotThrow(() -> {
            authService.setUserStatus("user@sumerge.com",true);
        });
    }

    @Test
    public void testIsAuthenticatedUserTrue(){
        assertNotNull(authService.isAuthenticatedUser("token1"));
    }
    @Test
    public void testIsAuthenticatedUserExpiredTokenFalse(){
         assertNull(authService.isAuthenticatedUser("token2"));
    }
    @Test
    public void testIsAuthenticatedLoggedOutFalse(){
        assertNull(authService.isAuthenticatedUser("token3"));
    }

}
