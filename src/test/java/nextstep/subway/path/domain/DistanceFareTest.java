package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceFareTest {
    @DisplayName("거리 대비 추가 요금을 계산할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"10:0", "11:100", "50:800", "51:900", "100:1500"}, delimiter = ':')
    void calculate(int value, int expectedFare) {
        Distance distance = new Distance(value);

        Fare distanceFare = DistanceFare.calculate(distance);

        assertThat(distanceFare).isEqualTo(new Fare(expectedFare));
    }
}
