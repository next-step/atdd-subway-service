package nextstep.subway.path.domain.policy.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdditionalLineFarePolicyTest {

    @DisplayName("라인 요금이 있을 경우 요금이 추가 된다.")
    @Test
    void calculate() {
        int lineFare = 900;
        int basicFare = 1250;
        AdditionalLineFarePolicy policy = new AdditionalLineFarePolicy(lineFare);

        int calculated = policy.calculate(basicFare);

        assertThat(calculated).isEqualTo(lineFare + basicFare);
    }
}