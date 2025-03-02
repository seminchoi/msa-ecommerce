package com.sem.ecommerce.auth.repository;

import com.sem.ecommerce.auth.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
}
