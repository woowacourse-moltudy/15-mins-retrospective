package wooteco.retrospective.domain.dao;

import wooteco.retrospective.domain.attendance.Time;

import java.util.List;
import java.util.Optional;

public interface TimeDao {
    List<Time> findAll();
    int update(Long timeId, Time time);
    Optional<Time> findById(long id);
}
