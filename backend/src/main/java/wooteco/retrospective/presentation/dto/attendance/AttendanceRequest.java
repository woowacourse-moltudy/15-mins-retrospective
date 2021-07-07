package wooteco.retrospective.presentation.dto.attendance;

public class AttendanceRequest {

    private long conferenceTimeId;
    private long memberId;

    private AttendanceRequest() {
    }

    public AttendanceRequest(long conferenceTimeId, long memberId) {
        this.conferenceTimeId = conferenceTimeId;
        this.memberId = memberId;
    }

    public long getConferenceTimeId() {
        return conferenceTimeId;
    }

    public long getMemberId() {
        return memberId;
    }
}
