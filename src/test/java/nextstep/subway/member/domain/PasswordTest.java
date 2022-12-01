package nextstep.subway.member.domain;

import nextstep.subway.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {
    @Test
    @DisplayName("패스워드 객체 생성")
    void createPassword() {
        // when
        Password actual = Password.from("password");

        // then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertThat(actual).isInstanceOf(Password.class)
        );
    }

    @Test
    @DisplayName("Null 입력시 패스워드 생성 실패")
    void createPasswordInputNull() {
        // when & then
        assertThatThrownBy(() -> Password.from(null))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("비밀번호를 입력해주세요.");
    }

    @Test
    @DisplayName("공백 입력시 패스워드 생성 실패")
    void createPasswordInputBlank() {
        // when & then
        assertThatThrownBy(() -> Password.from(" "))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("비밀번호를 입력해주세요.");
    }

    @ParameterizedTest
    @CsvSource(value = {"password:false", "wrong:true"}, delimiter = ':')
    @DisplayName("비밀번호 동일한지 확인 비밀번호 일치하지 않을 경우 true 반환")
    void checkPassword(String input, boolean expect) {
        // given
        Password password = Password.from("password");

        // when
        boolean actual = password.isWrongPassword(input);

        // then
        assertThat(actual).isEqualTo(expect);
    }
}
