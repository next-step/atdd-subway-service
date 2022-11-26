package nextstep.subway.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.common.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("비밀번호 관련 도메인 테스트")
public class PasswordTest {

    @DisplayName("비밀번호 생성 시 null이면 에러가 발생한다.")
    @Test
    void createPasswordThrowErrorWhenPasswordIsNull() {
        // when & throw
        assertThatThrownBy(() -> Password.from(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.비밀번호는_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("비밀번호 생성 시 비어있으면 에러가 발생한다.")
    @Test
    void createPasswordThrowErrorWhenPasswordIsEmpty() {
        // when & throw
        assertThatThrownBy(() -> Password.from(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.비밀번호는_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("비밀번호가 동일하면 동일한 객체이다.")
    @Test
    void equalPassword() {
        // given
        String password = "password";
        String duplicatePassword = "password";

        // when & then
        assertThat(Password.from(password)).isEqualTo(Password.from(duplicatePassword));
    }

    @DisplayName("비밀번호가 다르면 비밀번호 체크 시 에러를 반환한다.")
    @Test
    void checkPasswordThrowError() {
        // given
        String password = "password";
        String wrongPassword = "wrongPassword";

        // when & then
        assertThatThrownBy(() -> Password.from(password).checkPassword(wrongPassword))
                .isInstanceOf(AuthorizationException.class);
    }
}
