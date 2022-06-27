package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FareTest {

    @DisplayName("요금 음수일 경우 Exception 발생 확인")
    @Test
    void validate_empty() {
        // then
        assertThatThrownBy(() -> {
            new Fare(-1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("요금 차감")
    @Test
    void minus() {
        // given
        Fare fare = new Fare(3);

        // when
        Fare result = fare.minus(new Fare(1));

        // then
        assertThat(result.getFare()).isEqualTo(2);
    }

    @DisplayName("요금 차감시 차감할 금액이 더 클 경우 Exception 발생 확인")
    @Test
    void validate_invalidMinus() {
        // then
        assertThatThrownBy(() -> {
            new Fare(2).minus(new Fare(3));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("요금 퍼센트 할인")
    @Test
    void discountPercent() {
        // given
        Fare fare = new Fare(6);

        // when
        Fare result = fare.discountPercent(50);

        // then
        assertThat(result.getFare()).isEqualTo(3);
    }
}
