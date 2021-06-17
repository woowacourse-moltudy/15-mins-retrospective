package wooteco.retrospective.application.attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.retrospective.application.dto.AttendanceDto;
import wooteco.retrospective.application.dto.ConferenceTimeDto;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.ConferenceTime;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.infrastructure.dao.attendance.AttendanceDao;
import wooteco.retrospective.infrastructure.dao.attendance.ConferenceTimeDao;
import wooteco.retrospective.infrastructure.dao.member.MemberDao;
import wooteco.retrospective.presentation.dto.MembersDto;
import wooteco.retrospective.presentation.dto.attendance.AttendanceRequest;

@Transactional(readOnly = true)
@Service
public class AttendanceService {

    private final AttendanceDao attendanceDao;
    private final MemberDao memberDao;
    private final ConferenceTimeDao conferenceTimeDao;

    public AttendanceService(AttendanceDao attendanceDao, MemberDao memberDao, ConferenceTimeDao conferenceTimeDao) {
        this.attendanceDao = attendanceDao;
        this.memberDao = memberDao;
        this.conferenceTimeDao = conferenceTimeDao;
    }

    @Transactional
    public AttendanceDto postAttendance(AttendanceRequest attendanceRequest) {
        validateTime(attendanceRequest);

        Attendance attendance = createAttendance(attendanceRequest);
        Attendance newAttendance = attendanceDao.insert(attendance);

        return new AttendanceDto(
            newAttendance.getId(),
            newAttendance.getDate(),
            newAttendance.getMember(),
            newAttendance.getConferenceTime()
        );
    }

    private void validateTime(AttendanceRequest attendanceRequest) {
        if (attendanceDao.isExistSameTime(
            LocalDate.now(),
            attendanceRequest.getMemberId(),
            attendanceRequest.getConferenceTimeId()
        )) {
            throw new IllegalArgumentException("이미 등록된 시간입니다.");
        }
    }

    @Transactional
    public void deleteAttendance(AttendanceRequest attendanceRequest) {
        attendanceDao.delete(attendanceRequest.getMemberId(), attendanceRequest.getConferenceTimeId());
    }

    private Attendance createAttendance(AttendanceRequest attendanceRequest) {
        Member member = memberDao.findById(attendanceRequest.getMemberId())
            .orElseThrow(RuntimeException::new);
        ConferenceTime conferenceTime = conferenceTimeDao.findById(attendanceRequest.getConferenceTimeId())
            .orElseThrow(RuntimeException::new);
        return new Attendance(member, conferenceTime);
    }

    public ConferenceTimeDto findTimeById(long conferenceTimeId) {
        ConferenceTime conferenceTime = conferenceTimeDao.findById(conferenceTimeId)
            .orElseThrow(RuntimeException::new);

        return new ConferenceTimeDto(conferenceTime.getId(), conferenceTime.getConferenceTime());
    }

    public MembersDto findAttendanceByTime(ConferenceTimeDto conferenceTimeDto) {
        List<Member> members = attendanceDao.findByDateTime(LocalDate.now(), conferenceTimeDto.getId()).stream()
            .map(Attendance::getMember)
            .collect(Collectors.toList());
        return new MembersDto(members);
    }
}
