package wooteco.retrospective.presentation.dto;

import java.util.List;

import wooteco.retrospective.domain.member.Member;

public class MembersDto {
    private final List<Member> members;

    public MembersDto(List<Member> members) {
        this.members = members;
    }

    public List<Member> getMembers() {
        return members;
    }
}
