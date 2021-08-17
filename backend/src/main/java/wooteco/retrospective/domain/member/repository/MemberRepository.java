package wooteco.retrospective.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.retrospective.domain.member.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByName(String name);
}
