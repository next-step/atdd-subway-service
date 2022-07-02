package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFarePolicyTest {

    @DisplayName("거리별 요금 측정")
    @Test
    void calculate() {
        // given
        Distance distance = new Distance(58);

        // when
        Fare actual = DistanceFarePolicy.calculate(distance);
        Fare expected = new Fare(2150);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}