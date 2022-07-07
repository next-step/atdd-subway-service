package nextstep.subway.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {
    @DisplayName("10km초과 50km이하는, 5km 마다 추가요금은 100원이다.")
    @Test
    void create5kmExtraCharge() {
        Distance extraDistance = new Distance(10);

        assertThat(Fare.create5kmExtraCharge(extraDistance)).isEqualTo(new Fare(1450));
    }

    @DisplayName("50km초과시, 8km 마다 추가요금은 100원이다.")
    @Test
    void create8kmExtraCharge() {
        Fare fare = new Fare(1250);
        Distance extraDistance = new Distance(60);

        assertThat(Fare.create8kmExtraCharge(fare, extraDistance)).isEqualTo(new Fare(2050));
    }

    @DisplayName("요금을 할인한다")
    @Test
    void discountPercent() {
        Fare fare = new Fare(1000);
        int rate = 50;

        assertThat(fare.discountPercent(rate)).isEqualTo(new Fare(500));
    }
}
