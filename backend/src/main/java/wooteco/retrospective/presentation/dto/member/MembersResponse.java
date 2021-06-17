package wooteco.retrospective.presentation.dto.member;

import java.util.List;

import wooteco.retrospective.domain.member.Member;

public class MembersResponse {
    private final List<Member> members;

    public MembersResponse(List<Member> members) {
        this.members = members;
    }

    public List<Member> getMembers() {
        return members;
    }
}
