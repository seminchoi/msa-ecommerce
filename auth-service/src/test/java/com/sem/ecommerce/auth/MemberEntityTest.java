package com.sem.ecommerce.auth;

import com.sem.ecommerce.auth.domain.Member;
import com.sem.ecommerce.auth.infra.repository.JpaMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MemberEntityTest {
    @Autowired
    private JpaMemberRepository memberRepository;

    @Test
    @DisplayName("Member 저장 시 UUID가 정상적으로 생성되는지 확인")
    void saveMemberAndCheckUUID() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .name("홍길동")
                .password("password123")
                .phoneNumber("01025762075")
                .build();

        assertThat(member.getId()).isNull();

        // when
        memberRepository.save(member);

        // then
        assertThat(member.getId()).isNotNull();
    }
}
