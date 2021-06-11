package wooteco.retrospective.dao.member;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.time.Time;

@Repository
public class TimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Time> rowMapper = (resultSet, rowNumber) ->
        new Time(
            resultSet.getLong("id"),
            resultSet.getInt("time")
        );

    public TimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Time> findAll() {
        String query = "SELECT * FROM CONFERENCE_TIME";

        return jdbcTemplate.query(query, rowMapper);
    }

    public int update(Long timeId, Time time) {
        String query = "UPDATE CONFERENCE_TIME SET time = ? WHERE id = ?";

        return jdbcTemplate.update(query, time.getTime(), timeId);
    }

    public Optional<Time> findById(long id) {
        String query = "SELECT * FROM CONFERENCE_TIME WHERE id = ?";

        return jdbcTemplate.query(query, rowMapper, id).stream()
            .findAny();
    }
}
