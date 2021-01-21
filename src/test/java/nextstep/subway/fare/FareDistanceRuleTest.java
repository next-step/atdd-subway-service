package nextstep.subway.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nextstep.subway.fare.FareDistanceRule.findFareByDistance;
import static org.assertj.core.api.Assertions.assertThat;

class FareDistanceRuleTest {


    @DisplayName("요금을 계산한다 : 10km 이내")
    @ParameterizedTest
    @CsvSource(value = {"1:1250", "5:1250", "10:1250"}, delimiter = ':')
    void calculator_10km(int distance, int fare) {
        assertThat(findFareByDistance(distance)).isEqualTo(fare);
    }

    @DisplayName("요금을 계산한다 : 10km 초과 ~ 50km 이내")
    @ParameterizedTest
    @CsvSource(value = {"11:1350", "15:1350", "25:1550", "50:2050"}, delimiter = ':')
    void calculator_10km_50km(int distance, int fare) {
        assertThat(findFareByDistance(distance)).isEqualTo(fare);
    }

    @DisplayName("요금을 계산한다 : 50km 초과")
    @ParameterizedTest
    @CsvSource(value = {"51:2150", "58:2150", "59:2250"}, delimiter = ':')
    void calculator_50km(int distance, int fare) {
        assertThat(findFareByDistance(distance)).isEqualTo(fare);
    }
}