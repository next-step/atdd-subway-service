package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceFareTest {
    @DisplayName("거리별 요금을 계산 할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"10:1250", "11:1350", "50:2050", "51:2150", "100:2750"}, delimiter = ':')
    void calculate(int value, int expectedFare) {
        Distance distance = new Distance(value);

        Fare distanceFare = DistanceFare.calculate(distance);

        assertThat(distanceFare).isEqualTo(new Fare(expectedFare));
    }
}
