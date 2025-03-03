package com.sem.ecommerce.auth.service;

import com.sem.ecommerce.auth.controller.dto.CustomerSignUpRequest;
import com.sem.ecommerce.auth.domain.Member;
import com.sem.ecommerce.auth.domain.Role;
import com.sem.ecommerce.auth.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUpCustomer(CustomerSignUpRequest signUpRequest) {
        Member member = convertSecureMember(signUpRequest);

        memberRepository.findByEmail(signUpRequest.email()).ifPresent(m -> {
            throw new IllegalStateException("Email already in use");
        });

        memberRepository.save(member);
    }

    private Member convertSecureMember(CustomerSignUpRequest signUpRequest) {
        String securePassword = passwordEncoder.encode(signUpRequest.rawPassword());

        return Member.builder()
                .email(signUpRequest.email())
                .password(securePassword)
                .role(Role.ROLE_CUSTOMER)
                .name(signUpRequest.name())
                .phoneNumber(signUpRequest.phoneNumber())
                .build();
    }
}
