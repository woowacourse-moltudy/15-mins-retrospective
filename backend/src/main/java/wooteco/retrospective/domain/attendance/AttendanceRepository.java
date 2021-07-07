package wooteco.retrospective.domain.attendance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsAttendanceByMemberIdAndConferenceTimeIdAndDate(long memberId, long conferenceTimeId, LocalDate date);
    List<Attendance> findAttendancesByDateAndConferenceTimeIdAndPairNotNull(LocalDate date, long conferenceTimeId);
    List<Attendance> findAttendancesByDateAndConferenceTime(LocalDate date, ConferenceTime conferenceTime);
}
