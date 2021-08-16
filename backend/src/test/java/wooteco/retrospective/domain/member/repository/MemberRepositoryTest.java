package wooteco.retrospective.domain.member.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wooteco.retrospective.domain.member.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        Member 손너잘 = new Member("손너잘");
        memberRepository.save(손너잘);
    }

    @Test
    void findMemberByName() {
        Optional<Member> actual = memberRepository.findMemberByName("손너잘");
        assertThat(actual).isPresent();
    }
}
