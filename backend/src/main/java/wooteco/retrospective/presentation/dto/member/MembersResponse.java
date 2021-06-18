package wooteco.retrospective.presentation.dto.member;

import java.util.List;

public class MembersResponse {

    private final List<MemberResponse> members;

    public MembersResponse(List<MemberResponse> members) {
        this.members = members;
    }

    public List<MemberResponse> getMembers() {
        return members;
    }
}
