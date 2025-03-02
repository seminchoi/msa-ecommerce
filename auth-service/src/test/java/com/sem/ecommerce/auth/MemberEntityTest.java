package com.sem.ecommerce.auth;

import com.sem.ecommerce.auth.domain.Gender;
import com.sem.ecommerce.auth.domain.Member;
import com.sem.ecommerce.auth.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MemberEntityTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Member 저장 시 UUID가 정상적으로 생성되는지 확인")
    void saveMemberAndCheckUUID() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .gender(Gender.MALE)
                .name("홍길동")
                .password("password123")
                .username("hong123")
                .build();

        assertThat(member.getId()).isNull();

        // when
        memberRepository.save(member);

        // then
        assertThat(member.getId()).isNotNull();
    }
}
