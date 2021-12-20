package nextstep.subway.map.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayFareCalculatorTest {

    @DisplayName("10km 이내: 1,250원")
    @ParameterizedTest(name = "{displayName}{index} -> distance: {0}, expect: {1}")
    @CsvSource(value = {"0:1250", "5:1250", "10:1250"}, delimiter = ':')
    void fare_zeroToTen(int distance, int expect) {
        // when
        int fare = SubwayFareCalculator.calculate(distance);

        // then
        assertThat(fare).isEqualTo(expect);
    }

    @DisplayName("10~50km 이내: 5km 까지 마다 100원 추가")
    @ParameterizedTest(name = "{displayName}{index} -> distance: {0}, expect: {1}")
    @CsvSource(value = {"11:1350", "15:1350", "16:1450", "19:1450", "20:1450", "30:1650", "40:1850", "49:2050", "50:2050"}, delimiter = ':')
    void fare_overTenToFifty(int distance, int expect) {
        // when
        int fare = SubwayFareCalculator.calculate(distance);

        // then
        assertThat(fare).isEqualTo(expect);
    }

    @DisplayName("50km 초과 : 8km 까지 마다 100원 추가")
    @ParameterizedTest(name = "{displayName}{index} -> distance: {0}, expect: {1}")
    @CsvSource(value = {"51:2150", "58:2150", "59:2250", "65:2250", "66:2250", "74:2350", "82:2450", "89:2550", "90:2550"}, delimiter = ':')
    void fare_overFifty(int distance, int expect) {
        // when
        int fare = SubwayFareCalculator.calculate(distance);

        // then
        assertThat(fare).isEqualTo(expect);
    }
}
