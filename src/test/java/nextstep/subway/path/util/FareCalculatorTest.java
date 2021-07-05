package nextstep.subway.path.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    @DisplayName("10KM 이하 운임을 계산하여 요금을 리턴한다.")
    @ParameterizedTest
    @CsvSource({"8, 1250", "10, 1250"})
    void calculate_fare_with_under_ten_km(int distance, int totalFare) {
        int fare = FareCalculator.calculateFare(distance);
        assertThat(fare).isEqualTo(totalFare);
    }

    @DisplayName("10Km 초과 ~ 50Km까지의 요금을 리턴한다.")
    @ParameterizedTest
    @CsvSource({"12, 1350", "50, 2050"})
    void calculate_fare_with_under_fifty_km(int distance, int totalFare) {
        int fare = FareCalculator.calculateFare(distance);
        assertThat(fare).isEqualTo(totalFare);
    }

    @DisplayName("50Km 초과 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource({"51, 2150", "100, 2450"})
    void calculate_fare_with_over_fifty_km(int distance, int totalFare) {
        int fare = FareCalculator.calculateFare(distance);
        assertThat(fare).isEqualTo(totalFare);
    }
}
