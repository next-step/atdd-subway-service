package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DistanceFarePolicy 클래스 테스트")
class DistanceFarePolicyTest {

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
        Fare actual = DistanceFarePolicy.calculate(distanceValue);
        assertThat(actual.getValue()).isEqualTo(expectedFareValue);
    }
}
