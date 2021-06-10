package wooteco.retrospective.dao.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.retrospective.domain.member.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class MemberDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;
    private Member pika;
    private Member lion;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        pika = new Member("피카");
        lion = new Member("라이언");
    }

    @DisplayName("Member 추가")
    @Test
    void insert() {
        Member insertMember = memberDao.insert(pika);

        Member findMember = memberDao.findById(insertMember.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(insertMember).isEqualTo(findMember);
    }

    @DisplayName("중복된 Member를 추가할 시 에러")
    @Test
    void insertWithDuplicatedName() {
        memberDao.insert(pika);

        assertThatThrownBy(() -> memberDao.insert(pika))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("Member 삭제")
    @Test
    void delete() {
        Member insertMemberPika = memberDao.insert(pika);
        Member insertMemberLion = memberDao.insert(lion);

        memberDao.delete(insertMemberLion.getId());

        assertThat(memberDao.findAll()).containsExactly(insertMemberPika);
    }

    @DisplayName("Member 수정")
    @Test
    void update() {
        Member insertMemberPika = memberDao.insert(pika);

        memberDao.update(insertMemberPika.getId(), lion);

        Member updateMember = memberDao.findById(insertMemberPika.getId()).orElseThrow(IllegalArgumentException::new);

        assertThat(updateMember.getName()).isEqualTo(lion.getName());
    }

}
