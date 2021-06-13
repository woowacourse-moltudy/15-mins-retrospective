package wooteco.retrospective.application.member;

import org.springframework.stereotype.Service;
import wooteco.retrospective.exception.member.MemberNotFoundException;
import wooteco.retrospective.application.dto.MemberLoginDto;
import wooteco.retrospective.application.dto.MemberTokenDto;
import wooteco.retrospective.infrastructure.dao.member.MemberDao;
import wooteco.retrospective.domain.member.Member;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberTokenDto loginMember(MemberLoginDto requestDto) {
        if (memberDao.exists(requestDto.getName())) {
            return signInMember(requestDto);
        }
        return signUpMember(requestDto);
    }

    private MemberTokenDto signInMember(MemberLoginDto requestDto) {
        Member member = memberDao.findByName(requestDto.getName())
                .orElseThrow(MemberNotFoundException::new);
        return MemberTokenDto.from(member);
    }

    private MemberTokenDto signUpMember(MemberLoginDto requestDto) {
        Member member = memberDao.insert(requestDto.toMember());
        return MemberTokenDto.from(member);
    }
}
