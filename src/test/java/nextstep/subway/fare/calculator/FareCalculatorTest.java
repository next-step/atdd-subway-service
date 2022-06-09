package nextstep.subway.fare.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.fare.domain.FareType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FareCalculatorTest {

    @DisplayName("10km 이하 거리는 기본 요금이다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 4, 5, 6, 9, 10})
    void calculate01(int distance) {
        // given & when
        int fare = FareCalculator.calculate(distance);

        // then
        assertThat(fare).isEqualTo(FareType.BASIC.getFare());
    }

    @DisplayName("10km 초과 50km 까지의 거리는 초과한 거리 5km 당 100원이 추가된다.")
    @ParameterizedTest
    @CsvSource(value = {"11:100", "35:500", "50:800"}, delimiter = ':')
    void calculate02(int distance, int additionalFare) {
        // given & when
        int fare = FareCalculator.calculate(distance);

        // then
        assertThat(fare).isEqualTo(FareType.BASIC.getFare() + additionalFare);
    }

    @DisplayName("50km 초과 초과한 거리 8km 당 100원이 추가된다.")
    @ParameterizedTest
    @CsvSource(value = {"51:600", "70:800", "90:1000"}, delimiter = ':')
    void calculate03(int distance, int additionalFare) {
        // given & when
        int fare = FareCalculator.calculate(distance);

        // then
        assertThat(fare).isEqualTo(FareType.BASIC.getFare() + additionalFare);
    }
}