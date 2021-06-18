package wooteco.retrospective.domain.dao;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import wooteco.retrospective.domain.attendance.ConferenceTime;

public interface ConferenceTimeDao {

    List<ConferenceTime> findAll();

    int update(Long conferenceTimeId, ConferenceTime time);

    Optional<ConferenceTime> findById(long id);

    ConferenceTime insert(ConferenceTime conferenceTime);

    class Fake implements ConferenceTimeDao {

        private final Map<Long, ConferenceTime> cache = new HashMap<>();
        private long id = 1;

        public Fake() {
            cache.put(id, new ConferenceTime(1L, LocalTime.of(18, 0)));
            cache.put(++id, new ConferenceTime(2L, LocalTime.of(22, 0)));
        }

        @Override
        public List<ConferenceTime> findAll() {
            return new ArrayList<>(cache.values());
        }

        @Override
        public int update(Long timeId, ConferenceTime time) {
            if (!cache.containsKey(timeId)) {
                return 0;
            }

            cache.replace(timeId, time);

            return 1;
        }

        @Override
        public Optional<ConferenceTime> findById(long id) {
            return Optional.ofNullable(cache.getOrDefault(id, null));
        }

        @Override
        public ConferenceTime insert(ConferenceTime conferenceTime) {
            cache.put(id++, conferenceTime);
            return new ConferenceTime(
                id++,
                conferenceTime.getConferenceTime()
            );
        }
    }
}
