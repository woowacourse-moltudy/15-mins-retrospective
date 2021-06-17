package wooteco.retrospective.presentation.attendance;

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
import wooteco.retrospective.application.attendance.ConferenceTimeService;
import wooteco.retrospective.application.dto.ConferenceTimeDto;
import wooteco.retrospective.presentation.dto.MembersDto;
import wooteco.retrospective.presentation.dto.attendance.AttendanceByTimeResponse;
import wooteco.retrospective.presentation.dto.attendance.AttendanceRequest;
import wooteco.retrospective.presentation.dto.attendance.AttendanceResponse;

@RestController
@RequestMapping("/api/time")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final ConferenceTimeService conferenceTimeService;

    public AttendanceController(AttendanceService attendanceService, ConferenceTimeService conferenceTimeService) {
        this.attendanceService = attendanceService;
        this.conferenceTimeService = conferenceTimeService;
    }

    @PostMapping
    public ResponseEntity<AttendanceResponse> postTime(@Valid @RequestBody AttendanceRequest attendanceRequest) {
        ConferenceTimeDto conferenceTimeDto = conferenceTimeService.findConferenceTimeById(
            attendanceRequest.getConferenceTimeId());
        AttendanceResponse attendanceResponse =
            AttendanceResponse.of(attendanceService.postAttendance(conferenceTimeDto, attendanceRequest));

        return ResponseEntity.ok().body(attendanceResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTime(@Valid @RequestBody AttendanceRequest attendanceRequest) {
        ConferenceTimeDto conferenceTimeDto = conferenceTimeService.findConferenceTimeById(attendanceRequest.getConferenceTimeId());
        attendanceService.deleteAttendance(conferenceTimeDto, attendanceRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{conferenceTimeId}")
    public ResponseEntity<AttendanceByTimeResponse> getTime(@PathVariable("conferenceTimeId") long conferenceTimeId) {
        ConferenceTimeDto conferenceTimeDto = conferenceTimeService.findConferenceTimeById(conferenceTimeId);
        MembersDto membersDto = attendanceService.findAttendanceByTime(conferenceTimeDto);

        AttendanceByTimeResponse attendanceByTimeResponse
            = AttendanceByTimeResponse.of(conferenceTimeDto, membersDto);

        return ResponseEntity.ok().body(attendanceByTimeResponse);
    }
}
