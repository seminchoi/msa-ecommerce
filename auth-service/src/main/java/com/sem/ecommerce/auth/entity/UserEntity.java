package com.sem.ecommerce.auth.entity;

import com.sem.ecommerce.auth.domain.Gender;
import com.sem.ecommerce.auth.domain.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserEntity {
    private String username;
    private String password;
    private String name;
    private String email;
    private Gender gender;
    private Role role;

    @Builder
    public UserEntity(String email, Gender gender, String name, String password, String username) {
        this.email = email;
        this.gender = gender;
        this.name = name;
        this.password = password;
        this.username = username;
    }
}
