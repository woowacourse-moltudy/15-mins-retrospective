package wooteco.retrospective.dao.attendance;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import wooteco.retrospective.dao.member.MemberDao;
import wooteco.retrospective.domain.attendance.Attendance;

@Repository
public class AttendanceDao {

    private final JdbcTemplate jdbcTemplate;
    private MemberDao memberDao;
    private TimeDao timeDao;

    private final RowMapper<Attendance> rowMapper = (resultSet, rowNumber) ->
        new Attendance(
            resultSet.getLong("id"),
            resultSet.getObject("day", LocalDateTime.class),
            memberDao.findById(resultSet.getLong("member_id")).orElseThrow(RuntimeException::new),
            timeDao.findById(resultSet.getLong("time_id")).orElseThrow(RuntimeException::new)
        );

    public AttendanceDao(JdbcTemplate jdbcTemplate, MemberDao memberDao,
        TimeDao timeDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.memberDao = memberDao;
        this.timeDao = timeDao;
    }

    public Attendance insert(Attendance attendance) {
        String query = "INSERT INTO ATTENDANCE(member_id, time_id) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(query, new String[] {"id"});
            ps.setLong(1, attendance.getMemberId());
            ps.setLong(2, attendance.getTimeId());

            return ps;
        }, keyHolder);

        return new Attendance(
            Objects.requireNonNull(keyHolder.getKey()).longValue(),
            attendance.getDate(),
            attendance.getMember(),
            attendance.getTime()
        );
    }

    public Attendance findById(long id) {   //TODO: 예외처리
        String query = "SELECT * FROM ATTENDANCE WHERE id = ?";

        return jdbcTemplate.queryForObject(query, rowMapper, id);
    }

    public boolean isExistSameTime(long memberId, long timeId) {
        String query = "SELECT * FROM ATTENDANCE WHERE member_id = ? AND time_id = ?";

        List<Attendance> attendances = jdbcTemplate.query(query, rowMapper, memberId, timeId);
        return attendances.size() > 0;
    }

    public List<Attendance> findByDate(String date) {
        String query = "SELECT * FROM ATTENDANCE WHERE day = ?";

        return jdbcTemplate.query(query, rowMapper, date);
    }
}
