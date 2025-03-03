package com.sem.ecommerce.auth.domain.repository;

import com.sem.ecommerce.auth.domain.Member;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(UUID id);
    Optional<Member> findByEmail(String email);

}
