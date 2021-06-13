package wooteco.retrospective.presentation.dto.attendance;

public class AttendanceRequest {

    private final long timeId;
    private final long memberId;

    public AttendanceRequest(long timeId, long memberId) {
        this.timeId = timeId;
        this.memberId = memberId;
    }

    public long getTimeId() {
        return timeId;
    }

    public long getMemberId() {
        return memberId;
    }
}
