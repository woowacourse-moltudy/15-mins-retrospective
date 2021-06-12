package wooteco.retrospective.application.member;

import org.springframework.stereotype.Service;
import wooteco.exception.member.MemberNotFoundException;
import wooteco.retrospective.application.dto.MemberLoginRequestDto;
import wooteco.retrospective.application.dto.MemberLoginResponseDto;
import wooteco.retrospective.dao.member.MemberDao;
import wooteco.retrospective.domain.member.Member;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberLoginResponseDto loginMember(MemberLoginRequestDto requestDto) {
        if (memberDao.exists(requestDto.getName())) {
            return signInMember(requestDto);
        }
        return signUpMember(requestDto);
    }

    private MemberLoginResponseDto signInMember(MemberLoginRequestDto requestDto) {
        Member member = memberDao.findByName(requestDto.getName())
                .orElseThrow(MemberNotFoundException::new);
        return MemberLoginResponseDto.from(member);
    }

    private MemberLoginResponseDto signUpMember(MemberLoginRequestDto requestDto) {
        Member member = memberDao.insert(requestDto.toMember());
        return MemberLoginResponseDto.from(member);
    }
}
