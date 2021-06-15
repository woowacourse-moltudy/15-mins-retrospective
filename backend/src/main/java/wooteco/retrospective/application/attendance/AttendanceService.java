package wooteco.retrospective.application.attendance;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.ConferenceTime;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.infrastructure.dao.attendance.AttendanceDao;
import wooteco.retrospective.infrastructure.dao.attendance.ConferenceTimeDao;
import wooteco.retrospective.infrastructure.dao.member.MemberDao;
import wooteco.retrospective.presentation.dto.attendance.AttendanceRequest;

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
    public Attendance postAttendance(AttendanceRequest attendanceRequest) {
        validateTime(attendanceRequest);

        Attendance attendance = getAttendance(attendanceRequest);

        return attendanceDao.insert(attendance);
    }

    @Transactional
    public void deleteAttendance(AttendanceRequest attendanceRequest) {
        Attendance attendance = getAttendance(attendanceRequest);

        if (attendanceDao.delete(attendance) != 1) {
            throw new RuntimeException();
        }
    }

    @Transactional(readOnly = true)
    public ConferenceTime findTimeById(long conferenceTimeId) {
        return conferenceTimeDao.findById(conferenceTimeId)
            .orElseThrow(RuntimeException::new);
    }

    @Transactional(readOnly = true)
    public List<Member> findAttendanceByTime(ConferenceTime conferenceTime) {
        return attendanceDao.findByTime(LocalDate.now(), conferenceTime.getId()).stream()
            .map(Attendance::getMember)
            .collect(Collectors.toList());
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

    private Attendance getAttendance(AttendanceRequest attendanceRequest) {
        Member member = memberDao.findById(attendanceRequest.getMemberId())
            .orElseThrow(RuntimeException::new);
        ConferenceTime conferenceTime = conferenceTimeDao.findById(attendanceRequest.getConferenceTimeId())
            .orElseThrow(RuntimeException::new);
        return new Attendance(member, conferenceTime);
    }
}
