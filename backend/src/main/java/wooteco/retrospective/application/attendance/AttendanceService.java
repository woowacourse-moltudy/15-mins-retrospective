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
import wooteco.retrospective.infrastructure.dao.member.MemberDao;
import wooteco.retrospective.presentation.dto.MembersDto;
import wooteco.retrospective.presentation.dto.attendance.AttendanceRequest;

@Transactional(readOnly = true)
@Service
public class AttendanceService {

    private final AttendanceDao attendanceDao;
    private final MemberDao memberDao;

    public AttendanceService(AttendanceDao attendanceDao, MemberDao memberDao) {
        this.attendanceDao = attendanceDao;
        this.memberDao = memberDao;
    }

    @Transactional
    public AttendanceDto postAttendance(ConferenceTimeDto conferenceTimeDto, AttendanceRequest attendanceRequest) {
        validateTime(conferenceTimeDto, attendanceRequest);

        ConferenceTime conferenceTime = ConferenceTime.of(conferenceTimeDto);
        Attendance attendance = createAttendance(attendanceRequest, conferenceTime);
        Attendance newAttendance = attendanceDao.insert(attendance);

        return new AttendanceDto(
            newAttendance.getId(),
            newAttendance.getDate(),
            newAttendance.getMember(),
            newAttendance.getConferenceTime()
        );
    }

    private void validateTime(ConferenceTimeDto conferenceTimeDto, AttendanceRequest attendanceRequest) {
        Attendance attendance = createAttendance(attendanceRequest, ConferenceTime.of(conferenceTimeDto));

        if (attendanceDao.isExistSameTime(LocalDate.now(), attendance)) {
            throw new IllegalArgumentException("이미 등록된 시간입니다.");
        }
    }

    @Transactional
    public void deleteAttendance(ConferenceTimeDto conferenceTimeDto, AttendanceRequest attendanceRequest) {
        Attendance attendance = createAttendance(attendanceRequest, ConferenceTime.of(conferenceTimeDto));

        attendanceDao.delete(attendance);
    }

    private Attendance createAttendance(AttendanceRequest attendanceRequest, ConferenceTime conferenceTime) {
        Member member = memberDao.findById(attendanceRequest.getMemberId())
            .orElseThrow(RuntimeException::new);

        return new Attendance(member, conferenceTime);
    }

    public MembersDto findAttendanceByTime(ConferenceTimeDto conferenceTimeDto) {
        ConferenceTime conferenceTime = ConferenceTime.of(conferenceTimeDto);
        List<Member> members = attendanceDao.findByDateTime(LocalDate.now(), conferenceTime).stream()
            .map(Attendance::getMember)
            .collect(Collectors.toList());
        return new MembersDto(members);
    }
}
