package wooteco.retrospective.presentation.dto.attendance;

public class AttendanceRequest {

    private final long conferenceTimeId;
    private final long memberId;

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
