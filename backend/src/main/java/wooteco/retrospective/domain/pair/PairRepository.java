package wooteco.retrospective.domain.pair;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface PairRepository extends JpaRepository<Pair, Long> {

    @Query(value = "SELECT " +

            "P.group_id AS group_id, " +
            "M.id AS member_id, " +
            "M.name AS name, " +
            "A.id AS attendance_id, " +
            "A.date AS date " +

            "FROM PAIR P " +
            "INNER JOIN ATTENDANCE A " +
            "ON P.attendance_id = A.id " +
            "INNER JOIN MEMBER M " +
            "ON M.id = A.member_id " +

            "WHERE A.date=:date and A.conference_time_id = " +
            "(SELECT id FROM CONFERENCE_TIME WHERE conference_time =:time)", nativeQuery = true)
    List<Pair> findPairsByDateAndTime(LocalDate date, LocalTime time);
}
