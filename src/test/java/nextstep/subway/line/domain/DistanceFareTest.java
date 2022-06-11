package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DistanceFare 클래스 테스트")
class DistanceFareTest {

    @DisplayName("거리별 요금 계산을 한다.")
    @ParameterizedTest
    @CsvSource({
            "5, 1250",
            "10, 1250",
            "11, 1350",
            "25, 1550",
            "50, 2050",
            "51, 2150",
    })
    void test(int distanceValue, int expectedFareValue) {
        Distance distance = new Distance(distanceValue);
        Fare actual = DistanceFare.calculate(distance);
        assertThat(actual.getValue()).isEqualTo(expectedFareValue);
    }
}
