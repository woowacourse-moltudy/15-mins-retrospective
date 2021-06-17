package wooteco.retrospective.presentation.dto.attendance;

import java.util.List;

import wooteco.retrospective.application.dto.ConferenceTimeDto;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.presentation.dto.MembersDto;

public class AttendanceByTimeResponse {

    private Long conferenceTimeId;
    private List<Member> members;

    public AttendanceByTimeResponse() {
    }

    public AttendanceByTimeResponse(Long conferenceTimeId, List<Member> members) {
        this.conferenceTimeId = conferenceTimeId;
        this.members = members;
    }

    public static AttendanceByTimeResponse of(ConferenceTimeDto conferenceTime, MembersDto members) {
        return new AttendanceByTimeResponse(conferenceTime.getId(), members.getMembers());
    }

    public Long getConferenceTimeId() {
        return conferenceTimeId;
    }

    public List<Member> getMembers() {
        return members;
    }
}
