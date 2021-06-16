package wooteco.retrospective.presentation.dto.attendance;

import java.util.List;

import wooteco.retrospective.domain.attendance.ConferenceTime;
import wooteco.retrospective.domain.member.Member;

public class AttendanceByTimeResponse {

    private Long conferenceTimeId;
    private List<Member> members;

    public AttendanceByTimeResponse() {
    }

    public AttendanceByTimeResponse(Long conferenceTimeId, List<Member> members) {
        this.conferenceTimeId = conferenceTimeId;
        this.members = members;
    }

    public static AttendanceByTimeResponse of(ConferenceTime conferenceTime, List<Member> members) {
        return new AttendanceByTimeResponse(conferenceTime.getId(), members);
    }

    public Long getConferenceTimeId() {
        return conferenceTimeId;
    }

    public List<Member> getMembers() {
        return members;
    }
}
