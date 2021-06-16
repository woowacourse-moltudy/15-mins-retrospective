package wooteco.retrospective.application.member;

import org.springframework.stereotype.Service;
import wooteco.retrospective.application.dto.MemberLoginDto;
import wooteco.retrospective.application.dto.MemberTokenDto;
import wooteco.retrospective.domain.dto.MemberDTO;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.exception.NotFoundMemberException;
import wooteco.retrospective.presentation.dto.member.MemberResponse;
import wooteco.retrospective.utils.auth.JwtTokenProvider;
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

    private MemberTokenDto signUpMember(MemberLoginDto memberLoginDto) {
        Member member = memberDao.insert(memberLoginDto.toMember());
        String token = jwtTokenProvider.createToken(member.getName());
        return MemberTokenDto.from(token);
    }

    public MemberDTO findMemberByName(String name) {
        Member member = memberDao.findByName(name)
                .orElseThrow(NotFoundMemberException::new);
        return MemberDTO.from(member);
    }
}
