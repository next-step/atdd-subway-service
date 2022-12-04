package nextstep.subway.fare.policy;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceSurchargeTest {
    @ParameterizedTest
    @CsvSource(value = {"10:0", "15:100", "66:1000"}, delimiter = ':')
    @DisplayName("거리 기준 추가 요금 계산")
    void calculateSurchargeByDistance(int distance, int expect) {
        // given & when
        Fare actual = DistanceSurcharge.calculate(Distance.from(distance));

        // then
        assertThat(actual.value()).isEqualTo(expect);
    }
}
