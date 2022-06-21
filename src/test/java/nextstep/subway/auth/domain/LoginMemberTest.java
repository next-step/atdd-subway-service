package nextstep.subway.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class LoginMemberTest {
    @ParameterizedTest
    @ValueSource(ints = { 13, 14, 15, 16, 18 })
    @DisplayName("13세 이상~19세 미만은 청소년이다.")
    void 청소년(int age) {
        LoginMember loginMember = new LoginMember(0L, "email", age);
        assertThat(loginMember.isYouth()).isTrue();
        assertThat(loginMember.isChildren()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = { 6, 12 })
    @DisplayName("6세 이상~ 13세 미만은 어린이이다.")
    void 어린이(int age) {
        LoginMember loginMember = new LoginMember(0L, "email", age);
        assertThat(loginMember.isChildren()).isTrue();
        assertThat(loginMember.isYouth()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 5, 20, 21, 100 })
    @DisplayName("6세 미만 혹은 20세 이상은 어린이도 청소년도 아니다.")
    void X(int age) {
        LoginMember loginMember = new LoginMember(0L, "email", age);
        assertThat(loginMember.isYouth()).isFalse();
        assertThat(loginMember.isChildren()).isFalse();
    }

}
