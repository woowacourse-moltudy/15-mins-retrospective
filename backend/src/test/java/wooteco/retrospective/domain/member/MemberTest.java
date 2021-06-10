package wooteco.retrospective.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    @DisplayName("멤버 이름의 길이는 1~10글자이다, null이 들어갈 수 없다")
    @NullSource
    @ParameterizedTest
    @ValueSource(strings = {"열글자가넘는닉네임이뭐가있을까", ""})
    void invalidName(String name) {
        assertThatThrownBy(() -> new Member(name))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
