package wooteco.retrospective.dao.member;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.retrospective.domain.member.Member;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> rowMapper = (rs, rn) ->
            new Member(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("password")
            );

    public Member insert(Member member) {
        String query = "INSERT INTO MEMBER(name, password) VALUES(?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.jdbcTemplate.update(c -> {
            PreparedStatement ps = c.prepareStatement(query, new String[]{"id"});
            ps.setString(1, member.getName());
            ps.setString(2, member.getPassword());

            return ps;
        }, keyHolder);

        return new Member(keyHolder.getKey().longValue(), member.getName(), member.getPassword());
    }

    public Optional<Member> findById(Long id) {
        String query = "SELECT * FROM MEMBER WHERE id = ?";

        return this.jdbcTemplate.query(query, rowMapper, id)
                .stream()
                .findAny();
    }

    public int count() {
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
        String query = "UPDATE MEMBER SET name = ?, password = ? WHERE id = ?";

        return this.jdbcTemplate.update(query, memberLion.getName(), memberLion.getPassword(), id);
    }

}
