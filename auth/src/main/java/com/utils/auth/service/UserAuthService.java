package com.utils.auth.service;

import com.utils.auth.exception.UserNotFoundException;
import com.utils.auth.model.UserStatus;
import com.utils.auth.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthService {
    @Autowired
    UserStatusRepository userStatusRepository;

    public UserStatus isLoggedIn(String email){
        Optional<UserStatus> user = userStatusRepository.findUserStatusByEmail(email);
        if(user.isEmpty()){
            throw new UserNotFoundException();
        }
        return user.get();
    }
    public void setUserStatus(String email, boolean status){
        Optional<UserStatus> userOptional = userStatusRepository.findUserStatusByEmail(email);
        UserStatus user;
        if(userOptional.isEmpty()){
            user = new UserStatus(email,true);
            userStatusRepository.save(user);
            return;
        }
        user = userOptional.get();
        user.setLoggedIn(status);
        userStatusRepository.save(user);
    }
}
