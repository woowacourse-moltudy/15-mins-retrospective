package wooteco.retrospective.domain.pair.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wooteco.retrospective.domain.conference_time.ConferenceTime;
import wooteco.retrospective.domain.pair.Pair;

import java.time.LocalDate;
import java.util.List;

public interface PairRepository extends JpaRepository<Pair, Long> {
    @Query("select distinct p from Pair p join p.attendances a where a.date = :date and a.conferenceTime = :conferenceTime")
    List<Pair> findByDateAndConferenceTime(@Param("date") LocalDate date, @Param("conferenceTime") ConferenceTime conferenceTime);
}
