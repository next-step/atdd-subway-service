package nextstep.subway.member.domain;

import nextstep.subway.common.exception.InvalidParameterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {
    @Test
    @DisplayName("이메일 객체 생성")
    void createEmail() {
        // when
        Email actual = Email.from("email@email.com");

        // then
        Assertions.assertAll(
                () -> assertNotNull(actual),
                () -> assertThat(actual).isInstanceOf(Email.class)
        );
    }

    @Test
    @DisplayName("Null 입력시 이메일 생성 실패")
    void createEmailInputNull() {
        // when & then
        assertThatThrownBy(() -> Email.from(null))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("이메일을 입력해주세요.");
    }

    @Test
    @DisplayName("공백 입력시 이메일 생성 실패")
    void createEmailInputBlank() {
        // when & then
        assertThatThrownBy(() -> Email.from(""))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("이메일을 입력해주세요.");
    }

    @Test
    @DisplayName("이메일 형식 맞지 않을 경우 이메일 생성 실패")
    void createEmailInputIncorrectEmailFormat() {
        // when & then
        assertThatThrownBy(() -> Email.from("wooowaemail.com"))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("이메일 형식이 맞지 않습니다.");
    }
}
