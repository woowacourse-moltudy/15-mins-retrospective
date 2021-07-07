package wooteco.retrospective.presentation.attendance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.retrospective.application.attendance.AttendanceService;
import wooteco.retrospective.application.attendance.ConferenceTimeService;
import wooteco.retrospective.application.dto.ConferenceTimeDto;
import wooteco.retrospective.application.dto.MembersDto;
import wooteco.retrospective.presentation.dto.TimesResponse;
import wooteco.retrospective.presentation.dto.attendance.AttendanceByTimeResponse;
import wooteco.retrospective.presentation.dto.attendance.AttendanceRequest;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/time")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final ConferenceTimeService conferenceTimeService;

    public AttendanceController(AttendanceService attendanceService, ConferenceTimeService conferenceTimeService) {
        this.attendanceService = attendanceService;
        this.conferenceTimeService = conferenceTimeService;
    }

    @GetMapping
    public ResponseEntity<TimesResponse> getTime() {
        List<ConferenceTimeDto> conferenceTimeDtos = conferenceTimeService.findAllTime();

        TimesResponse timesResponse = TimesResponse.of(conferenceTimeDtos);
        return ResponseEntity.ok().body(timesResponse);
    }

    @PostMapping
    public ResponseEntity<Void> postTime(@Valid @RequestBody AttendanceRequest attendanceRequest) {
        ConferenceTimeDto conferenceTimeDto = conferenceTimeService.findConferenceTimeById(attendanceRequest.getConferenceTimeId());

        attendanceService.postAttendance(conferenceTimeDto, attendanceRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTime(@Valid @RequestBody AttendanceRequest attendanceRequest) {
        ConferenceTimeDto conferenceTimeDto = conferenceTimeService.findConferenceTimeById(attendanceRequest.getConferenceTimeId());

        attendanceService.deleteAttendance(conferenceTimeDto, attendanceRequest);

        return ResponseEntity.noContent().build();
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
