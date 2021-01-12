package nextstep.subway.path.application.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    @DisplayName("기본운임(10㎞ 이내)")
    @Test
    void calculateDistanceFare1() {
        //given
        int distance = 8;

        //when
        int fare = FareCalculator.calculateFare(distance);

        //then
        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("10km 초과 ∼ 50km 까지 (5km마다 100원)")
    @Test
    void calculateDistanceFare2() {
        //given
        int distance = 30;

        //when
        int fare = FareCalculator.calculateFare(distance);

        //then - fare = 1250 + 600(100 * 6)
        assertThat(fare).isEqualTo(1850);
    }

    @DisplayName("50km 초과 시 (8km마다 100원)")
    @Test
    void calculateDistanceFare3() {
        //given
        int distance = 80;

        //when
        int fare = FareCalculator.calculateFare(distance);

        //then - fare = 1250 + 1000(100 * 10)
        assertThat(fare).isEqualTo(2250);
    }
}