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
    private String email;
    private String password;
    private String name;
    private Role role;
    private String phoneNumber;

    @Builder
    public Member(String email, UUID id, String name, String password, String phoneNumber, Role role) {
        this.email = email;
        this.id = id;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}