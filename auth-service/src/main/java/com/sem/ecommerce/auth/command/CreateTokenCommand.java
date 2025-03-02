package com.sem.ecommerce.auth.command;

import com.sem.ecommerce.auth.domain.Gender;
import com.sem.ecommerce.auth.domain.Role;
import com.sem.ecommerce.auth.domain.Member;
import lombok.Builder;

@Builder
public record CreateTokenCommand(
        String username,
        String name,
        String email,
        Gender gender,
        Role role
) {
    public static CreateTokenCommand from(Member member) {
        return CreateTokenCommand.builder()
                .username(member.getUsername())
                .name(member.getName())
                .email(member.getEmail())
                .gender(member.getGender())
                .role(member.getRole())
                .build();
    }
}
