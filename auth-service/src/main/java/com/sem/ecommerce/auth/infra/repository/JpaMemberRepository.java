package com.sem.ecommerce.auth.infra.repository;

import com.sem.ecommerce.auth.domain.Member;
import com.sem.ecommerce.auth.domain.repository.MemberRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaMemberRepository extends JpaRepository<Member, UUID>, MemberRepository {
}
