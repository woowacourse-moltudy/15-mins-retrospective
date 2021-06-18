package wooteco.retrospective.application.attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.retrospective.application.dto.ConferenceTimeDto;
import wooteco.retrospective.application.dto.MembersDto;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.ConferenceTime;
import wooteco.retrospective.domain.dao.AttendanceDao;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.exception.AlreadyExistTimeException;
import wooteco.retrospective.exception.NotFoundMemberException;
import wooteco.retrospective.infrastructure.dao.member.MemberDao;
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
    public void postAttendance(ConferenceTimeDto conferenceTimeDto, AttendanceRequest attendanceRequest) {
        validateTime(conferenceTimeDto, attendanceRequest);

        ConferenceTime conferenceTime = ConferenceTime.of(conferenceTimeDto);
        Attendance attendance = createAttendance(attendanceRequest, conferenceTime);

        attendanceDao.insert(attendance);
    }

    private void validateTime(ConferenceTimeDto conferenceTimeDto, AttendanceRequest attendanceRequest) {
        Attendance attendance = createAttendance(attendanceRequest, ConferenceTime.of(conferenceTimeDto));

        if (attendanceDao.isExistSameTime(LocalDate.now(), attendance)) {
            throw new AlreadyExistTimeException();
        }
    }

    @Transactional
    public void deleteAttendance(ConferenceTimeDto conferenceTimeDto, AttendanceRequest attendanceRequest) {
        Attendance attendance = createAttendance(attendanceRequest, ConferenceTime.of(conferenceTimeDto));

        attendanceDao.delete(attendance);
    }

    private Attendance createAttendance(AttendanceRequest attendanceRequest, ConferenceTime conferenceTime) {
        Member member = memberDao.findById(attendanceRequest.getMemberId())
            .orElseThrow(NotFoundMemberException::new);

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
