package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nextstep.subway.fare.domain.Fare.DEFAULT_FARE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("거리 별 요금 테스트")
class FaresByDistanceTest {
    @CsvSource(delimiterString = ":",
            value = {
                    "14:1250",
                    "15:1350",
                    "49:1950",
                    "50:2050",
                    "57:2050",
                    "58:2150",
                    "59:2150",
                    "66:2250"
            })
    @ParameterizedTest
    void calculate_성공(int distanceValue, int expectedFareValue) {
        // Given
        Distance distance = new Distance(distanceValue);
        Fare expectedFare = new Fare(expectedFareValue);

        // when, then
        assertThat(FaresByDistance.calculate(DEFAULT_FARE, distance))
                .isEqualTo(expectedFare);
    }
}
