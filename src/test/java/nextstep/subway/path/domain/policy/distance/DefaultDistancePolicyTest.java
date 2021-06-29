package nextstep.subway.path.domain.policy.distance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultDistancePolicyTest {

    @DisplayName("기본 요금 계산")
    @Test
    void calculate() {
        int basicFare = 1250;
        DefaultDistancePolicy policy = new DefaultDistancePolicy();

        int calculated = policy.calculate(basicFare);

        assertThat(calculated).isEqualTo(basicFare);
    }
}
