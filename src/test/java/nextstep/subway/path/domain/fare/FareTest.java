package nextstep.subway.path.domain.fare;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    @DisplayName("요금 더하기")
    @Test
    void add() {
        Fare fare = Fare.from(1);

        assertThat(fare.add(Fare.from(2))).isEqualTo(Fare.from(3));
    }

    @DisplayName("요금 빼기")
    @Test
    void minus() {
        Fare fare = Fare.from(3);

        assertThat(fare.minus(Fare.from(2))).isEqualTo(Fare.from(1));
    }

    @DisplayName("요금 곱하기")
    @Test
    void multiply() {
        Fare fare = Fare.from(10);

        assertThat(fare.multiply(BigDecimal.valueOf(0.5))).isEqualTo(Fare.from(5));
    }
}