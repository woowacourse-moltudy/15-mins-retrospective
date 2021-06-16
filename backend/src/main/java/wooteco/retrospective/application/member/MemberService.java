package wooteco.retrospective.application.member;

import org.springframework.stereotype.Service;
import wooteco.retrospective.application.dto.MemberLoginDto;
import wooteco.retrospective.application.dto.MemberTokenDto;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.exception.NotFoundMemberException;
import wooteco.retrospective.infrastructure.auth.JwtTokenProvider;
import wooteco.retrospective.infrastructure.dao.member.MemberDao;

@Service
public class MemberService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberTokenDto loginMember(MemberLoginDto requestDto) {
        return memberDao.findByName(requestDto.getName())
                .map(member -> jwtTokenProvider.createToken(member.getName()))
                .map(MemberTokenDto::from)
                .orElseGet(() -> signUpMember(requestDto));
    }

    private MemberTokenDto signUpMember(MemberLoginDto requestDto) {
        Member member = memberDao.insert(requestDto.toMember());
        String token = jwtTokenProvider.createToken(member.getName());
        return MemberTokenDto.from(token);
    }

    public Member findMemberByToken(String token) {
        String name = jwtTokenProvider.getPayload(token);

        return memberDao.findByName(name)
                .orElseThrow(NotFoundMemberException::new);
    }

}
