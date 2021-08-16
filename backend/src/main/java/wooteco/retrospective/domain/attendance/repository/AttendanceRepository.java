package wooteco.retrospective.domain.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.retrospective.domain.attendance.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

}
