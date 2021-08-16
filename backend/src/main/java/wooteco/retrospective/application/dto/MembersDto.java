package wooteco.retrospective.application.dto;

import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.presentation.dto.member.MemberResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MembersDto {

    private final List<Member> members;

    public MembersDto(List<Member> members) {
        this.members = members;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<MemberResponse> getMemberResponses() {
        return members.stream()
            .map(member -> new MemberResponse(member.getId(), member.getName()))
            .collect(Collectors.toList());
    }
}
