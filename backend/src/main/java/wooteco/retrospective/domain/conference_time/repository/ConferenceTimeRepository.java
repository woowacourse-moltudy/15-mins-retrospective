package wooteco.retrospective.domain.conference_time.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.retrospective.domain.conference_time.ConferenceTime;

public interface ConferenceTimeRepository extends JpaRepository<ConferenceTime, Long> {

}
