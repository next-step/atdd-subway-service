package nextstep.subway.line.domain;

import nextstep.subway.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareTest {
    @DisplayName("추가요금이 0보다 작으면 예외가 발생한다")
    @Test
    void valueException() {
        // when & then
        assertThatThrownBy(() -> new Fare(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.FARE_BIGGEST_THAN_ZERO.getMessage());
    }
}
