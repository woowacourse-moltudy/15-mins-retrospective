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
import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.presentation.dto.attendance.AttendanceByTimeResponse;
import wooteco.retrospective.presentation.dto.attendance.AttendanceRequest;
import wooteco.retrospective.presentation.dto.attendance.AttendanceResponse;

@RestController
@RequestMapping("/api")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/time")
    public ResponseEntity<AttendanceResponse> postTime(@Valid @RequestBody AttendanceRequest attendanceRequest) {
        AttendanceResponse attendanceResponse = AttendanceResponse.of(
            attendanceService.postAttendance(attendanceRequest));

        return ResponseEntity.ok().body(attendanceResponse);
    }

    @DeleteMapping("/time")
    public ResponseEntity<Void> deleteTime(@Valid @RequestBody AttendanceRequest attendanceRequest) {
        attendanceService.deleteAttendance(attendanceRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/time/{time}")
    public ResponseEntity<AttendanceByTimeResponse> getTime(@PathVariable("time") long timeId) {
        Time time = attendanceService.findTimeById(timeId);
        List<Member> members = attendanceService.findAttendanceByTime(time);

        AttendanceByTimeResponse attendanceByTimeResponse = AttendanceByTimeResponse.of(
            time, members
        );

        return ResponseEntity.ok().body(attendanceByTimeResponse);
    }
}
