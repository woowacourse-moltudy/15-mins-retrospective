package wooteco.retrospective.dto;

import javax.validation.constraints.NotEmpty;

public class AttendanceRequest {

    @NotEmpty(message = "날짜를 입력해주세요.")
    private final String date;
    private final long timeId;
    private final long memberId;

    public AttendanceRequest(String date, long timeId, long memberId) {
        this.date = date;
        this.timeId = timeId;
        this.memberId = memberId;
    }

    public String getDate() {
        return date;
    }

    public long getTimeId() {
        return timeId;
    }

    public long getMemberId() {
        return memberId;
    }

}