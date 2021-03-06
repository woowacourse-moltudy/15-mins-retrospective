package wooteco.retrospective.application.attendance;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.retrospective.application.dto.ConferenceTimeDto;
import wooteco.retrospective.application.dto.MembersDto;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.repository.AttendanceRepository;
import wooteco.retrospective.domain.conference_time.ConferenceTime;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.member.repository.MemberRepository;
import wooteco.retrospective.exception.AlreadyExistTimeException;
import wooteco.retrospective.exception.NotFoundMemberException;
import wooteco.retrospective.presentation.dto.attendance.AttendanceRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, MemberRepository memberRepository) {
        this.attendanceRepository = attendanceRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void postAttendance(ConferenceTimeDto conferenceTimeDto, AttendanceRequest attendanceRequest) {
        ConferenceTime conferenceTime = ConferenceTime.of(conferenceTimeDto);
        Attendance attendance = createAttendance(attendanceRequest, conferenceTime);

        validateTime(attendance);

        attendanceRepository.save(attendance);
    }

    private void validateTime(Attendance attendance) {
        Member member = memberRepository.findById(attendance.getMemberId())
                .orElseThrow(NotFoundMemberException::new);

        boolean present = attendanceRepository
                .findAttendanceByDateAndMember(LocalDate.now(), member)
                .isPresent();

        if (present) {
            throw new AlreadyExistTimeException();
        }
    }

    @Transactional
    public void deleteAttendance(ConferenceTimeDto conferenceTimeDto, AttendanceRequest attendanceRequest) {
        Attendance attendance = createAttendance(attendanceRequest, ConferenceTime.of(conferenceTimeDto));

        attendanceRepository.delete(attendance);
    }

    private Attendance createAttendance(AttendanceRequest attendanceRequest, ConferenceTime conferenceTime) {
        Member member = memberRepository.findById(attendanceRequest.getMemberId())
                .orElseThrow(NotFoundMemberException::new);

        return new Attendance(member, conferenceTime);
    }

    public MembersDto findAttendanceByTime(ConferenceTimeDto conferenceTimeDto) {
        ConferenceTime conferenceTime = ConferenceTime.of(conferenceTimeDto);
        List<Member> members = attendanceRepository
                .findAttendanceByDateAndConferenceTime(LocalDate.now(), conferenceTime)
                .stream()
                .map(Attendance::getMember)
                .collect(Collectors.toList());
        return new MembersDto(members);
    }
}
