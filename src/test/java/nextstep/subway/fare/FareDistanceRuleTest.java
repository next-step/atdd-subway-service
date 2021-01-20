package nextstep.subway.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.fare.FareDistanceRule.findFareByDistance;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FareDistanceRuleTest {


    @DisplayName("요금을 계산한다 : 10km 이내")
    @Test
    void calculator_10km() {
        assertAll(
                () -> assertThat(findFareByDistance(1)).isEqualTo(1250),
                () -> assertThat(findFareByDistance(5)).isEqualTo(1250),
                () -> assertThat(findFareByDistance(10)).isEqualTo(1250)
        );
    }

    @DisplayName("요금을 계산한다 : 10km 초과 ~ 50km 이내")
    @Test
    void calculator_10km_50km() {
        assertAll(
                () -> assertThat(findFareByDistance(11)).isEqualTo(1350),
                () -> assertThat(findFareByDistance(15)).isEqualTo(1350),
                () -> assertThat(findFareByDistance(25)).isEqualTo(1550),
                () -> assertThat(findFareByDistance(50)).isEqualTo(2050)
        );
    }

    @DisplayName("요금을 계산한다 : 50km 초과")
    @Test
    void calculator_50km() {
        assertAll(
                () -> assertThat(findFareByDistance(51)).isEqualTo(2150),
                () -> assertThat(findFareByDistance(58)).isEqualTo(2150),
                () -> assertThat(findFareByDistance(59)).isEqualTo(2250)
        );
    }
}