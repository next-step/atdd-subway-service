package nextstep.subway.member.domain;

import nextstep.subway.auth.application.AuthorizationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class PasswordTest {

    @DisplayName("비밀번호는 null이 될 수 없다.")
    @Test
    void 비밀번호은_null이_될_수_없다() {
        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> Password.from(null));
    }

    @DisplayName("비밀번호은 빈 값이 될 수 없다.")
    @Test
    void 비밀번호은_빈_값이_될_수_없다() {
        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> Password.from(""));
    }

    @DisplayName("비밀번호가 다르면 AuthorizationException 예외가 발생한다.")
    @Test
    void 비밀번호_비교() {
        // given
        Password password = Password.from("password");

        // when, then
        assertThatThrownBy(() -> password.equalsPassword("new_password"))
                .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("비밀번호 생성 성공")
    @Test
    void 비밀번호_생성_성공() {
        // given
        Password password = Password.from("password");

        // when, then
        assertThat(password.value()).isEqualTo("password");
    }
}
