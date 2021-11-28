package nextstep.subway.fare.calculator;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.fare.domain.FareType;

class FareCalculatorTest {

    @DisplayName("0 ~ 10km 이하 거리구간의 기준 요금 조회를 한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 9, 10})
    void calculate1(int distance) {
        // when
        int actualFare = FareCalculator.calculate(distance);

        // then
        assertThat(actualFare).isEqualTo(FareType.BASIC.getFare());
    }

    @DisplayName("10km 초과 ~ 50km 이하 거리구간의 기준 요금 조회를 한다.")
    @ParameterizedTest
    @CsvSource(value = {"11,1350", "15,1350", "16,1450", "18,1450", "45,1950", "46,2050", "50,2050"})
    void calculate2(int distance, int expectedFare) {
        // when
        int actualFare = FareCalculator.calculate(distance);

        // then
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @DisplayName("50km 초과 거리구간의 기준 요금 조회를 한다.")
    @ParameterizedTest
    @CsvSource(value = {"51,2150", "58,2150", "59,2250", "66,2250", "90,2550", "95,2650", "100,2750"})
    void calculate3(int distance, int expectedFare) {
        // when
        int actualFare = FareCalculator.calculate(distance);

        // then
        assertThat(actualFare).isEqualTo(expectedFare);
    }
}