package wooteco.retrospective.application.member;

import org.springframework.stereotype.Service;
import wooteco.retrospective.application.dto.MemberLoginDto;
import wooteco.retrospective.application.dto.MemberTokenDto;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.infrastructure.dao.member.MemberDao;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberTokenDto loginMember(MemberLoginDto requestDto) {
        return memberDao.findByName(requestDto.getName())
                .map(MemberTokenDto::from)
                .orElseGet(() -> signUpMember(requestDto));
    }

    private MemberTokenDto signUpMember(MemberLoginDto requestDto) {
        Member member = memberDao.insert(requestDto.toMember());
        return MemberTokenDto.from(member);
    }
}
