package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareDistanceTest {
    @DisplayName("기본 지하철 요금은 1250원이다.")
    @Test
    void basicFare() {
        // when
        Fare result = FareDistance.calculate(0);

        // then
        assertThat(result.value()).isEqualTo(1_250);
    }

    @DisplayName("10km 초과 ~ 50km 까지는 5km 마다 100원이 추가된다")
    @ParameterizedTest
    @CsvSource(value = {"11:1350", "15:1350", "16:1450", "20:1450", "21:1550"}, delimiter = ':')
    void middleFare(int distance, int fare) {
        // when
        Fare result = FareDistance.calculate(distance);

        // then
        assertThat(result.value()).isEqualTo(fare);
    }

    @DisplayName("50km 초과 ~ 178km 까지는 8km 마다 100원이 추가된다")
    @ParameterizedTest
    @CsvSource(value = {"51:2150", "58:2150", "59:2250", "66:2250", "67:2350"}, delimiter = ':')
    void longFare(int distance, int fare) {
        // when
        Fare result = FareDistance.calculate(distance);

        // then
        assertThat(result.value()).isEqualTo(fare);
    }
}
