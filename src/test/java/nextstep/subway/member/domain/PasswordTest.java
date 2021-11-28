package nextstep.subway.member.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@DisplayName("패스워드")
class PasswordTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Password.from("password"));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 값 으로 객체화 할 수 없다.")
    @NullAndEmptySource
    @DisplayName("패스워드 값은 필수")
    void instance_emptyValue_thrownIllegalArgumentException(String value) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Password.from(value))
            .withMessageEndingWith("패스워드 값은 필수입니다.");
    }
}
