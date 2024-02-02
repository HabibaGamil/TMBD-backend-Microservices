package com.sumerge.userservice.mapper;

import com.sumerge.userservice.model.dto.UserDto;
import com.sumerge.userservice.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public User toUser(UserDto userDTO) {
        return new User(
                userDTO.getName(),
                userDTO.getPassword(),
                userDTO.getEmail()
        );
    }
}
