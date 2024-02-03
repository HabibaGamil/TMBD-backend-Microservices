package com.utils.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserAuthService userAuthService;
    @Autowired
    JwtService jwtService;

    public void setUserStatus(String email, boolean status){

        userAuthService.setUserStatus(email,status);
    }

    public UserDetails isAuthenticatedUser(String token){

        try {
            boolean isTokenExpired = jwtService.isTokenExpired(token);
            String email = jwtService.extractUsername(token);
            UserDetails loggedUser = userAuthService.isLoggedIn(email);
            if (isTokenExpired || loggedUser==null) {
                return null;
            }
            return loggedUser;
        }catch( Exception exc ){
            return null;
        }

    }
    public UserDetails findByEmail(String email){
        return null;

    }



}
