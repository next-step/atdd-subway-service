package nextstep.subway.fare.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareTest {
    @DisplayName("요금은 0원 보다 적을 수 없다")
    @Test
    void less_than_zero_fare() {
        // given & when & then
        assertThatThrownBy(() -> Fare.from(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}