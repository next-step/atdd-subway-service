package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFareCalculatorTest {

    @DisplayName("거리가 10km 이하면 기본요금이다.")
    @ParameterizedTest
    @ValueSource(doubles = {1, 2, 9, 10})
    void distanceDefaultFare(double distance) {
        assertThat(DistanceFareCalculator.calculateByDistance(distance)).isEqualTo(new Fare(0));
    }

    // 요금이 바뀌면 테스트가 깨진다.
    @DisplayName("거리가 10~50km면 5km당 추가요금 발생한다")
    @ParameterizedTest
    @CsvSource(value = {"11:0", "12:0", "15:100", "20:200", "50:800"}, delimiter = ':')
    void distanceMoreTenlessFifty(double distance, int expected) {
        assertThat(DistanceFareCalculator.calculateByDistance(distance)).isEqualTo(new Fare(expected));
    }

    // 요금이 바뀌면 테스트가 깨진다.
    @DisplayName("거리가 50km 초과하면 8km당 추가요금 발생한다")
    @ParameterizedTest
    @CsvSource(value = {"50:800", "55:800", "58:900", "66:1000"}, delimiter = ':')
    void distanceMoreFifty(double distance, int expected) {
        assertThat(DistanceFareCalculator.calculateByDistance(distance)).isEqualTo(new Fare(expected));
    }
}
