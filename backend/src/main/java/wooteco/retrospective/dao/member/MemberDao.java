package wooteco.retrospective.dao.member;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import wooteco.retrospective.domain.member.Member;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> rowMapper = (rs, rn) ->
        new Member(
            rs.getLong("id"),
            rs.getString("name")
        );

    public Member insert(Member member) {
        String query = "INSERT INTO MEMBER(name) VALUES(?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.jdbcTemplate.update(c -> {
            PreparedStatement ps = c.prepareStatement(query, new String[] {"id"});
            ps.setString(1, member.getName());

            return ps;
        }, keyHolder);

        return new Member(keyHolder.getKeyAs(Long.class), member.getName());
    }

    public Optional<Member> findById(Long id) {
        String query = "SELECT * FROM MEMBER WHERE id = ?";

        return this.jdbcTemplate.query(query, rowMapper, id)
            .stream()
            .findAny();
    }

    public int size() {
        String query = "SELECT count(*) FROM MEMBER";

        return this.jdbcTemplate.queryForObject(query, Integer.class);
    }

    public int delete(Long id) {
        String query = "DELETE FROM MEMBER WHERE id = ?";

        return this.jdbcTemplate.update(query, id);
    }

    public List<Member> findAll() {
        String query = "SELECT * FROM MEMBER";

        return this.jdbcTemplate.query(query, rowMapper);
    }

    public int update(Long id, Member memberLion) {
        String query = "UPDATE MEMBER SET name = ? WHERE id = ?";

        return this.jdbcTemplate.update(query, memberLion.getName(), id);
    }

}
