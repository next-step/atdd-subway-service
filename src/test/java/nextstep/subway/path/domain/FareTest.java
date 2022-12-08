package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
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
}
