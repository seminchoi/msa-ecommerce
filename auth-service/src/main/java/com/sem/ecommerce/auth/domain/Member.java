package com.sem.ecommerce.auth.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    private UUID id;
    private String username;
    private String password;
    private String name;
    private String email;
    private Gender gender;
    private Role role;

    @Builder
    public Member(String email, Gender gender, String name, String password, Role role, String username) {
        this.email = email;
        this.gender = gender;
        this.name = name;
        this.password = password;
        this.role = role;
        this.username = username;
    }
}