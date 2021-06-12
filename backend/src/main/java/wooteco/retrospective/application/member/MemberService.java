package wooteco.retrospective.application.member;

import org.springframework.stereotype.Service;
import wooteco.dto.member.MemberLoginRequest;
import wooteco.dto.member.MemberLoginResponse;
import wooteco.exception.member.MemberNotFoundException;
import wooteco.retrospective.dao.member.MemberDao;
import wooteco.retrospective.domain.member.Member;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberLoginResponse loginMember(MemberLoginRequest request) {
        if (memberDao.exists(request.getName())) {
            Optional<Member> member = memberDao.findByName(request.getName());
            return MemberLoginResponse.of(member.orElseThrow(MemberNotFoundException::new));
        }
        Member member = memberDao.insert(request.toMember());
        return MemberLoginResponse.of(member);
    }
}
