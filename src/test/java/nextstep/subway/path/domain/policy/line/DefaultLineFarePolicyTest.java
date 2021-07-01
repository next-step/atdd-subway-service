package nextstep.subway.path.domain.policy.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultLineFarePolicyTest {

    @DisplayName("라인 요금 없을 경우 요금을 계산한다.")
    @Test
    void calculate() {
        int basicFare = 1250;
        DefaultLineFarePolicy policy = new DefaultLineFarePolicy();

        int calculated = policy.calculate(basicFare);

        assertThat(calculated).isEqualTo(basicFare);
    }
}
