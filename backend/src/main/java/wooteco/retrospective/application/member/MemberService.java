package wooteco.retrospective.application.member;

import org.springframework.stereotype.Service;
import wooteco.dto.member.MemberLoginRequest;
import wooteco.dto.member.MemberLoginResponse;
import wooteco.exception.member.MemberNotFoundException;
import wooteco.retrospective.dao.member.MemberDao;
import wooteco.retrospective.domain.member.Member;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberLoginResponse loginMember(MemberLoginRequest request) {
        if (memberDao.exists(request.getName())) {
            return signInMember(request);
        }
        return signUpMember(request);
    }

    private MemberLoginResponse signInMember(MemberLoginRequest request) {
        Member member = memberDao.findByName(request.getName())
                .orElseThrow(MemberNotFoundException::new);
        return MemberLoginResponse.from(member);
    }

    private MemberLoginResponse signUpMember(MemberLoginRequest request) {
        Member member = memberDao.insert(request.toMember());
        return MemberLoginResponse.from(member);
    }
}
