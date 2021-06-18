package wooteco.retrospective.domain.dao;

import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.ConferenceTime;
import wooteco.retrospective.exception.NotFoundTimeException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public interface AttendanceDao {

    Attendance insert(Attendance attendance);

    Attendance findById(long id);

    boolean isExistSameTime(LocalDate date, Attendance attendance);

    List<Attendance> findByDate(LocalDate date);

    List<Attendance> findByDateTime(LocalDate date, ConferenceTime conferenceTime);

    void delete(Attendance attendance);

    class Fake implements AttendanceDao {

        private final Map<Long, Attendance> cache = new HashMap<>();
        private long id = 1;

        @Override
        public Attendance insert(Attendance attendance) {
            cache.put(id, attendance);
            return new Attendance(
                    id++,
                    attendance.getDate(),
                    attendance.getMember(),
                    attendance.getConferenceTime()
            );
        }

        @Override
        public Attendance findById(long id) {
            if (!cache.containsKey(id)) {
                throw new RuntimeException();
            }

            return cache.get(id);
        }

        @Override
        public boolean isExistSameTime(LocalDate date, Attendance attendance) {
            return cache.values().stream()
                    .filter(attendance1 -> attendance1.getMemberId() == attendance.getMemberId())
                    .filter(attendance1 -> attendance1.getConferenceTimeId() == attendance.getConferenceTimeId())
                    .anyMatch(attendance1 -> attendance1.getDate().equals(date));
        }

        @Override
        public List<Attendance> findByDate(LocalDate date) {
            return cache.values().stream()
                    .filter(attendance -> attendance.getDate().equals(date))
                    .collect(toList());
        }

        @Override
        public List<Attendance> findByDateTime(LocalDate date, ConferenceTime conferenceTime) {
            return cache.values().stream()
                .filter(attendance -> attendance.getDate().equals(date))
                .filter(attendance -> attendance.getConferenceTimeId() == conferenceTime.getId())
                .collect(toList());
        }

        @Override
        public void delete(Attendance attendance) {
            Attendance foundAttendance = cache.values().stream()
                .filter(attendance1 -> attendance1.getMemberId() == attendance.getMemberId())
                .filter(attendance1 -> attendance1.getConferenceTimeId() == attendance.getConferenceTimeId())
                .findAny()
                .orElseThrow(NotFoundTimeException::new);
            cache.remove(foundAttendance.getId());
        }
    }
}
