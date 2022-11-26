package nextstep.subway.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.common.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("이메일 관련 도메인 테스트")
public class EmailTest {

    @DisplayName("이메일 생성 시 null이면 에러가 발생한다.")
    @Test
    void createEmailThrowErrorWhenEmailIsNull() {
        // when & throw
        assertThatThrownBy(() -> Email.from(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.이메일은_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("이메일 생성 시 비어있으면 에러가 발생한다.")
    @Test
    void createEmailThrowErrorWhenEmailIsEmpty() {
        // when & throw
        assertThatThrownBy(() -> Email.from(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.이메일은_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("이메일을 생성하면 이메일을 조회할 수 있다.")
    @Test
    void createEmail() {
        // given
        String actual = "email@email.com";

        // when
        Email email = Email.from(actual);

        // then
        assertThat(email.value()).isEqualTo(actual);
    }

    @DisplayName("이메일이 동일하면 동일한 객체이다.")
    @Test
    void equalEmail() {
        // given
        String email = "email@email.com";
        String duplicateEmail = "email@email.com";

        // when & then
        assertThat(Email.from(email)).isEqualTo(Email.from(duplicateEmail));
    }
}
