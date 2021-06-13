package wooteco.retrospective.domain.dao;

import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.pair.Pairs;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PairDao {
    Pairs insert(Pairs pairs);
    Optional<Pairs> findByDateAndTime(LocalDateTime date, Time time);
}
