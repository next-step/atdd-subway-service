package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금")
class FareTest {

    @DisplayName("요금을 계산한다. / 10㎞ 이내일 경우 1,250원")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 10})
    void calculate_10km(int distance) {
        Fare fare = new Fare(distance);
        assertThat(fare.calculate()).isEqualTo(1250);
    }

    @DisplayName("요금을 계산한다. / 10km 초과 ∼ 50km 까지(5km 마다 100원)")
    @ParameterizedTest
    @CsvSource(value = {"11:1350", "14:1350", "16:1450", "50:2050"}, delimiter = ':')
    void calculate_10km_50km(int distance, int result) {
        Fare fare = new Fare(distance);
        assertThat(fare.calculate()).isEqualTo(result);
    }

    @DisplayName("요금을 계산한다. / 50km 초과 ∼ 50km 까지(8km 마다 100원)")
    @ParameterizedTest
    @CsvSource(value = {"51:2150", "58:2150", "59:2250", "67:2350"}, delimiter = ':')
    void calculate_50km(int distance, int result) {
        Fare fare = new Fare(distance);
        assertThat(fare.calculate()).isEqualTo(result);
    }
}
