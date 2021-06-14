package wooteco.retrospective.presentation.dto.attendance;

import java.util.List;

import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.member.Member;

public class AttendanceByTimeResponse {

    private Long time;
    private List<Member> members;

    public AttendanceByTimeResponse() {
    }

    public AttendanceByTimeResponse(Long time, List<Member> members) {
        this.time = time;
        this.members = members;
    }

    public static AttendanceByTimeResponse of(Time time,
        List<Member> members) {
        return new AttendanceByTimeResponse(time.getId(), members);
    }

    public Long getTime() {
        return time;
    }

    public List<Member> getMembers() {
        return members;
    }
}
