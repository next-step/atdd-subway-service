package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareCalculatorTest {

    @DisplayName("요금 계산 - 기본요금")
    @Test
    void calculateFare_MinimumFare() {
        // given
        Fare fare = FareCalculator.calculateFare(5);

        // when
        int result = fare.getFare();

        // then
        assertThat(result).isEqualTo(FareCalculator.MINIMUM_FARE);
    }

    @DisplayName("요금 계산 - 10km 초과 50km 이하")
    @Test
    void calculateFare_TenToFifty() {
        // given
        Fare fare = FareCalculator.calculateFare(30);
        int expected = 1_650;

        // when
        int result = fare.getFare();

        // then
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("요금 계산 - 50km 초과")
    @Test
    void calculateFare_OverFifty() {
        // given
        Fare fare = FareCalculator.calculateFare(60);
        int expected = 2_250;

        // when
        int result = fare.getFare();

        // then
        assertThat(result).isEqualTo(expected);
    }
}
