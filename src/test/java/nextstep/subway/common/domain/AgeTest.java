package nextstep.subway.common.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("나이")
class AgeTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Age.from(1));
    }

    @Test
    @DisplayName("나이는 반드시 양수")
    void instance_negativeValue_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Age.from(Integer.MIN_VALUE))
            .withMessage("나이는 반드시 양수이어야 합니다.");
    }
}
