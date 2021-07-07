package wooteco.retrospective.application.member;

import org.springframework.stereotype.Service;
import wooteco.retrospective.application.dto.MemberDTO;
import wooteco.retrospective.application.dto.MemberLoginDto;
import wooteco.retrospective.application.dto.MemberTokenDto;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.member.MemberRepository;
import wooteco.retrospective.exception.NotFoundMemberException;
import wooteco.retrospective.utils.auth.JwtTokenProvider;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository,
                         JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberTokenDto loginMember(MemberLoginDto requestDto) {
        return memberRepository.findMemberByName(requestDto.getName())
                .map(member -> jwtTokenProvider.createToken(member.getName()))
                .map(MemberTokenDto::from)
                .orElseGet(() -> signUpMember(requestDto));
    }

    private MemberTokenDto signUpMember(MemberLoginDto memberLoginDto) {
        Member member = memberRepository.save(memberLoginDto.toMember());
        String token = jwtTokenProvider.createToken(member.getName());

        return MemberTokenDto.from(token);
    }

    public MemberDTO findMemberByName(String name) {
        Member member = memberRepository.findMemberByName(name)
                .orElseThrow(NotFoundMemberException::new);

        return MemberDTO.from(member);
    }
}
