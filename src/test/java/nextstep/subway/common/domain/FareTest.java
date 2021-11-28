package nextstep.subway.common.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Disabled("요금")
class FareTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Fare.from(Integer.MAX_VALUE));
    }

    @Test
    @DisplayName("요금은 반드시 양수")
    void instance_negativeValue_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Fare.from(Integer.MIN_VALUE))
            .withMessage("요금은 반드시 0 또는 양수이어야 합니다.");
    }

}
