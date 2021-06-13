package wooteco.retrospective.domain.dao;

import wooteco.retrospective.domain.attendance.Attendance;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public interface AttendanceDao {
    Attendance insert(Attendance attendance);

    Attendance findById(long id);

    boolean isExistSameTime(long memberId, long timeId);

    List<Attendance> findByDate(LocalDate date);

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
                    attendance.getTime()
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
        public boolean isExistSameTime(long memberId, long timeId) {
            return cache.values().stream()
                    .filter(attendance -> attendance.getMemberId() == memberId)
                    .filter(attendance -> attendance.getTimeId() == timeId)
                    .anyMatch(attendance -> attendance.getDate().equals(LocalDate.now()));
        }

        @Override
        public List<Attendance> findByDate(LocalDate date) {
            return cache.values().stream()
                    .filter(attendance -> attendance.getDate().equals(date))
                    .collect(toList());
        }
    }
}
