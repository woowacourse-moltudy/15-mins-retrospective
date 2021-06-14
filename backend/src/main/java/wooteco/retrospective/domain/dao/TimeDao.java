package wooteco.retrospective.domain.dao;

import wooteco.retrospective.domain.attendance.Time;

import java.time.LocalTime;
import java.util.*;

public interface TimeDao {
    List<Time> findAll();

    int update(Long timeId, Time time);

    Optional<Time> findById(long id);

    class Fake implements TimeDao {

        private final Map<Long, Time> cache;

        public Fake() {
            cache = new HashMap<>();

            cache.put(1L, new Time(1L, LocalTime.of(18, 0)));
            cache.put(2L, new Time(2L, LocalTime.of(22, 0)));
        }

        @Override
        public List<Time> findAll() {
            return new ArrayList<>(cache.values());
        }

        @Override
        public int update(Long timeId, Time time) {
            if (!cache.containsKey(timeId)) {
                return 0;
            }

            cache.replace(timeId, time);

            return 1;
        }

        @Override
        public Optional<Time> findById(long id) {
            return Optional.ofNullable(cache.getOrDefault(id, null));
        }
    }
}
