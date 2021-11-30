package nextstep.subway.common.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("이메일")
class EmailTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Email.from("email@email.com"));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 값 으로 객체화 할 수 없다.")
    @NullAndEmptySource
    @DisplayName("이메일 값은 필수")
    void instance_emptyValue_thrownIllegalArgumentException(String value) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Email.from(value))
            .withMessageEndingWith("이메일 값은 필수입니다.");
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 값 으로 객체화 할 수 없다.")
    @ValueSource(strings = {"abc", "가나다", "abc.com"})
    @DisplayName("반드시 이메일 형식")
    void instance_notEmailFormat_thrownIllegalArgumentException(String value) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Email.from(value))
            .withMessageEndingWith("이메일 형식이 잘못되었습니다.");
    }
}
