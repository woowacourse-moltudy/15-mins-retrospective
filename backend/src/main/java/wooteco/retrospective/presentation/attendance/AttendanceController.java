package wooteco.retrospective.presentation.attendance;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.retrospective.application.attendance.AttendanceService;
import wooteco.retrospective.domain.attendance.ConferenceTime;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.presentation.dto.attendance.AttendanceByTimeResponse;
import wooteco.retrospective.presentation.dto.attendance.AttendanceRequest;
import wooteco.retrospective.presentation.dto.attendance.AttendanceResponse;

@RestController
@RequestMapping("/api/time")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public ResponseEntity<AttendanceResponse> postTime(@Valid @RequestBody AttendanceRequest attendanceRequest) {
        AttendanceResponse attendanceResponse = AttendanceResponse.of(
            attendanceService.postAttendance(attendanceRequest));

        return ResponseEntity.ok().body(attendanceResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTime(@Valid @RequestBody AttendanceRequest attendanceRequest) {
        attendanceService.deleteAttendance(attendanceRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{conferenceTimeId}")
    public ResponseEntity<AttendanceByTimeResponse> getTime(@PathVariable("conferenceTimeId") long conferenceTimeId) {
        ConferenceTime conferenceTime = attendanceService.findTimeById(conferenceTimeId);
        List<Member> members = attendanceService.findAttendanceByTime(conferenceTime);

        AttendanceByTimeResponse attendanceByTimeResponse
            = AttendanceByTimeResponse.of(conferenceTime, members);

        return ResponseEntity.ok().body(attendanceByTimeResponse);
    }
}
