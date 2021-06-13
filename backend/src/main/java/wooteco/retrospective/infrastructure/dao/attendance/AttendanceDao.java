package wooteco.retrospective.infrastructure.dao.attendance;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.infrastructure.dao.member.MemberDao;

@Repository
public class AttendanceDao {

    private final JdbcTemplate jdbcTemplate;
    private MemberDao memberDao;
    private TimeDao timeDao;

    private final RowMapper<Attendance> rowMapper = (resultSet, rowNumber) ->
        new Attendance(
            resultSet.getLong("id"),
            resultSet.getObject("date", LocalDate.class),
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
        String query = "INSERT INTO ATTENDANCE(date, member_id, time_id) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(query, new String[] {"id"});
            ps.setDate(1, Date.valueOf(attendance.getDate()));
            ps.setLong(2, attendance.getMemberId());
            ps.setLong(3, attendance.getTimeId());

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

    public boolean isExistSameTime(LocalDate date, long memberId, long timeId) {
        String query = "SELECT EXISTS (SELECT * FROM ATTENDANCE WHERE date = ? AND member_id = ? AND time_id = ?)";

        return jdbcTemplate.queryForObject(query, boolean.class, date, memberId, timeId);
    }

    public List<Attendance> findByDate(LocalDate date) {
        String query = "SELECT * FROM ATTENDANCE WHERE date = ?";

        return jdbcTemplate.query(query, rowMapper, date);
    }
}
