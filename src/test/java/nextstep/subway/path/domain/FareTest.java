package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {
    @DisplayName("요금이 음수일 경우 예외가 발생한다.")
    @Test
    void negative() {
        assertThatThrownBy(() -> new Fare(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요금은 음수일 수 없습니다.");
    }

    @DisplayName("요금을 더할 수 있다.")
    @Test
    void add() {
        Fare fare = new Fare(1000);
        Fare extraFare = new Fare(500);

        assertThat(fare.add(extraFare)).isEqualTo(new Fare(1500));
    }

    @DisplayName("요금을 뺄 수 있다.")
    @Test
    void subtract() {
        Fare fare = new Fare(1500);
        Fare extraFare = new Fare(500);

        assertThat(fare.subtract(extraFare)).isEqualTo(new Fare(1000));
    }

    @DisplayName("요금을 뺀 결과가 음수일 경우 예외가 발생한다.")
    @Test
    void subtractNegative() {
        Fare fare = new Fare(1500);
        Fare extraFare = new Fare(2000);

        assertThatThrownBy(() -> fare.subtract(extraFare))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요금은 음수일 수 없습니다.");
    }
}
