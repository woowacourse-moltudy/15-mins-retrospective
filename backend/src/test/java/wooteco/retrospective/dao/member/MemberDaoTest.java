package wooteco.retrospective.dao.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.retrospective.domain.member.Member;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class MemberDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    void insert() {
        Member member = new Member("피카", "123");
        Member newMember = memberDao.insert(member);

        Member findById = memberDao.findById(newMember.getId()).orElseThrow(IllegalArgumentException::new);

        assertThat(newMember).isEqualTo(findById);
    }

}