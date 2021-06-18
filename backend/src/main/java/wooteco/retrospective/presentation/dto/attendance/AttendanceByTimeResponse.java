package wooteco.retrospective.presentation.dto.attendance;

import wooteco.retrospective.application.dto.ConferenceTimeDto;
import wooteco.retrospective.application.dto.MembersDto;
import wooteco.retrospective.presentation.dto.member.MembersResponse;

public class AttendanceByTimeResponse {

    private long conferenceTimeId;
    private MembersResponse members;

    public AttendanceByTimeResponse() {
    }

    public AttendanceByTimeResponse(long conferenceTimeId, MembersResponse members) {
        this.conferenceTimeId = conferenceTimeId;
        this.members = members;
    }

    public static AttendanceByTimeResponse of(ConferenceTimeDto conferenceTime, MembersDto membersDto) {
        MembersResponse membersResponse = new MembersResponse(membersDto.getMembers());

        return new AttendanceByTimeResponse(conferenceTime.getId(), membersResponse);
    }

    public Long getConferenceTimeId() {
        return conferenceTimeId;
    }

    public MembersResponse getMembers() {
        return members;
    }
}
