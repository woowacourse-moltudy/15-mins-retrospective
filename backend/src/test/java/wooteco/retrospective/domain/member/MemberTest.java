package wooteco.retrospective.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    @DisplayName("멤버 이름은 10글자를 넘을 수 없다.")
    @NullSource
    @ParameterizedTest
    @ValueSource(strings = {"열글자가넘는닉네임이뭐가있을까", ""})
    void invalidName(String name) {
        assertThatThrownBy(() -> new Member(name, "123"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("멤버 패스워드는 10글자를 넘을 수 없다.")
    @NullSource
    @ParameterizedTest
    @ValueSource(strings = {"열글자가넘는패스워드가뭐가있을까", ""})
    void invalidPassword(String password) {
        assertThatThrownBy(() -> new Member("피카", password))
                .isInstanceOf(IllegalArgumentException.class);
    }

}