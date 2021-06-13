package wooteco.retrospective.domain.dao;

import wooteco.retrospective.domain.attendance.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceDao {
    Attendance insert(Attendance attendance);
    Attendance findById(long id);
    boolean isExistSameTime(long memberId, long timeId);
    List<Attendance> findByDate(LocalDate date);
}
