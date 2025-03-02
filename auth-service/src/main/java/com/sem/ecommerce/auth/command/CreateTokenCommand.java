package com.sem.ecommerce.auth.command;

import com.sem.ecommerce.auth.domain.Gender;
import com.sem.ecommerce.auth.domain.Role;
import com.sem.ecommerce.auth.entity.UserEntity;
import lombok.Builder;

@Builder
public record CreateTokenCommand(
        String username,
        String name,
        String email,
        Gender gender,
        Role role
) {
    public static CreateTokenCommand from(UserEntity userEntity) {
        return CreateTokenCommand.builder()
                .username(userEntity.getUsername())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .gender(userEntity.getGender())
                .role(userEntity.getRole())
                .build();
    }
}
