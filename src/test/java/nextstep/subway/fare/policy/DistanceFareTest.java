package nextstep.subway.fare.policy;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceFareTest {
    @ParameterizedTest
    @CsvSource(value = {"10:1250", "15:1350", "66:2250"}, delimiter = ':')
    @DisplayName("거리 기준 요금 계산")
    void calculateFareNyDistance(int distance, int expect) {
        // given & when
        Fare actual = DistanceFare.calculate(Distance.from(distance));

        // then
        assertThat(actual.value()).isEqualTo(expect);
    }
}
