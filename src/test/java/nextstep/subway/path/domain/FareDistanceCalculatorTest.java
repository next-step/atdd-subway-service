package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareDistanceCalculatorTest {


    @DisplayName("거리를 받아 요금 계산")
    @ParameterizedTest
    @CsvSource(value = {"20:200", "50:800", "60:1000"}, delimiter = ':')
    void calculate(int distance, int expectedFare) {
        // when
        Fare fare = FareDistanceCalculator.calculate(distance);

        // then
        assertThat(fare).isNotNull();
        assertThat(fare.getValue()).isEqualTo(expectedFare);
    }
}
