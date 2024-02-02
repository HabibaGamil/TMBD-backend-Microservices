package com.sumerge.userservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name="_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User  {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    String name;

    @NotNull
    String email;

    @NotNull
    String password;

    Boolean isLoggedIn= false;

    public User( String name, String password, String email) {
        this.name = name;
        this.email = email;
        this.password=password;
    }


}
