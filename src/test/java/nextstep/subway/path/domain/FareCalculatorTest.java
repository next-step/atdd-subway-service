package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    @DisplayName("거리에 따른 요금 계산")
    @ParameterizedTest
    @CsvSource(value = {"1:1250", "10:1250", "20:1450", "50:2050", "58:2150", "59:2250"}, delimiter = ':')
    void calculateOf(int distance, int fare) {
        assertThat(FareCalculator.calculateFareOf(distance)).isEqualTo(fare);
    }
}
