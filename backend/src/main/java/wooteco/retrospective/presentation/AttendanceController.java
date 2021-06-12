package wooteco.retrospective.presentation;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.retrospective.application.AttendanceService;
import wooteco.retrospective.dto.AttendanceRequest;
import wooteco.retrospective.dto.AttendanceResponse;

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
}
