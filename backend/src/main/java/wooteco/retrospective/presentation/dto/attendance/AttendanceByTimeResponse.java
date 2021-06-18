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

    public static AttendanceByTimeResponse of(ConferenceTimeDto conferenceTimeDto, MembersDto membersDto) {
        return new AttendanceByTimeResponse(
            conferenceTimeDto.getId(),
            new MembersResponse(membersDto.getMemberResponses())
        );
    }

    public Long getConferenceTimeId() {
        return conferenceTimeId;
    }

    public MembersResponse getMembers() {
        return members;
    }
}
