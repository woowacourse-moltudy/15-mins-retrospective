package wooteco.retrospective.domain.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import wooteco.retrospective.domain.conference_time.ConferenceTime;
import wooteco.retrospective.domain.pair.Pair;
import wooteco.retrospective.domain.pair.Pairs;

public interface PairDao {

    Pairs insert(Pairs pairs);

    Optional<Pairs> findByDateAndTime(LocalDate date, ConferenceTime time);

    class Fake implements PairDao {

        private final List<Pairs> cache = new ArrayList<>();

        @Override
        public Pairs insert(Pairs pairs) {
            cache.add(pairs);
            return pairs;
        }

        @Override
        public Optional<Pairs> findByDateAndTime(LocalDate date, ConferenceTime time) {
            return cache.stream()
                .filter(isContainsAnyMatchedPairWith(date, time))
                .findAny();
        }

        private Predicate<Pairs> isContainsAnyMatchedPairWith(LocalDate date, ConferenceTime time) {
            return pairs -> pairs.getPairs().stream()
                .anyMatch(isExistsAnyMatchedPairWith(date, time));
        }

        private Predicate<Pair> isExistsAnyMatchedPairWith(LocalDate date, ConferenceTime time) {
            return pair -> pair.getAttendances().stream()
                .filter(attendance -> attendance.isAttendAt(date))
                .anyMatch(attendance -> attendance.isAttendAt(time));
        }
    }
}
