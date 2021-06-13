package wooteco.retrospective.domain.dao;

import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.pair.Pairs;

import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.toList;

public interface PairDao {
    Pairs insert(Pairs pairs);

    Optional<Pairs> findByDateAndTime(LocalDate date, Time time);

    class Fake implements PairDao {

        private final List<Pairs> cache = new ArrayList<>();

        @Override
        public Pairs insert(Pairs pairs) {
            cache.add(pairs);
            return pairs;
        }

        @Override
        public Optional<Pairs> findByDateAndTime(LocalDate date, Time time) {
            return cache.stream()
                    .filter(
                            pairs -> pairs.getPairs().stream()
                                    .anyMatch(
                                            pair -> pair.getAttendances().stream()
                                                    .filter(attendance -> attendance.getDate().equals(date))
                                                    .anyMatch(attendance -> attendance.getTime().equals(time))
                                    )
                    ).findAny();
        }
    }
}
