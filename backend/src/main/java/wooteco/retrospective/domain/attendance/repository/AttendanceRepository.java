package wooteco.retrospective.domain.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.conference_time.ConferenceTime;
import wooteco.retrospective.domain.member.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findAttendanceByDateAndMember(LocalDate date, Member member);
    List<Attendance> findAttendanceByDateAndConferenceTime(LocalDate date, ConferenceTime conferenceTime);
}
