package wooteco.retrospective.domain.pair.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.retrospective.domain.pair.Pair;

public interface PairRepository extends JpaRepository<Pair, Long> {

}
