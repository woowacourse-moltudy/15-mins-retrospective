package wooteco.retrospective.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.retrospective.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
