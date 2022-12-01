package nextstep.subway.line.domain;

import nextstep.subway.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
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

    @DisplayName("요금을 더할 수 있다")
    @Test
    void add() {
        // given
        Fare fare = new Fare(10);

        // when
        Fare result = fare.add(new Fare(10));

        // then
        assertThat(result.value()).isEqualTo(20);
    }

    @DisplayName("요금을 뺼 수 있다")
    @Test
    void subtract() {
        // given
        Fare fare = new Fare(10);

        // when
        Fare result = fare.subtract(new Fare(10));

        // then
        assertThat(result.value()).isEqualTo(0);
    }
}
