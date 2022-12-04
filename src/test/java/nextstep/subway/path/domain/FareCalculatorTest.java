package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FareCalculatorTest {

    @Test
    @DisplayName("10km 이하의 요금을 계산한다")
    void 요금_계산_10km_이하() {
        assertThat(FareCalculator.calculateFare(new Distance(10))).isEqualTo(1250);
    }

    @Test
    @DisplayName("10km 초과 50km 이하의 요금을 계산한다")
    void 요금_계산_10km_초과_50km_이하() {
        assertAll(
                () -> assertThat(FareCalculator.calculateFare(new Distance(11))).isEqualTo(1350),
                () -> assertThat(FareCalculator.calculateFare(new Distance(15))).isEqualTo(1350),
                () -> assertThat(FareCalculator.calculateFare(new Distance(50))).isEqualTo(2050)
        );
    }

    @Test
    @DisplayName("50km 초과의 요금을 계산한다")
    void 요금_계산_50km_초과() {
        assertAll(
                () -> assertThat(FareCalculator.calculateFare(new Distance(51))).isEqualTo(2150),
                () -> assertThat(FareCalculator.calculateFare(new Distance(131))).isEqualTo(3150),
                () -> assertThat(FareCalculator.calculateFare(new Distance(1000))).isEqualTo(13950)
        );
    }
}
