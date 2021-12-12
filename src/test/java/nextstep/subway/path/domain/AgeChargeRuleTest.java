package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AgeChargeRuleTest {

    @DisplayName("나이에 맞게 규칙이 분배되는지 검증")
    @Test
    void ruleByAge() {
        AgeChargeRule rule = AgeChargeRule.valueOf(0);

        assertThat(rule).isEqualTo(AgeChargeRule.DEFAULT);

        rule = AgeChargeRule.valueOf(6);

        assertThat(rule).isEqualTo(AgeChargeRule.CHILD);

        rule = AgeChargeRule.valueOf(12);

        assertThat(rule).isEqualTo(AgeChargeRule.CHILD);

        rule = AgeChargeRule.valueOf(13);

        assertThat(rule).isEqualTo(AgeChargeRule.YOUTH);

        rule = AgeChargeRule.valueOf(18);

        assertThat(rule).isEqualTo(AgeChargeRule.YOUTH);

        rule = AgeChargeRule.valueOf(19);

        assertThat(rule).isEqualTo(AgeChargeRule.DEFAULT);
    }

    @DisplayName("계산된 금액이 잘 나오는지 검증")
    @Test
    void calculateFare() {
        long then = AgeChargeRule.calculateFare(1250L, 6);

        assertThat(then).isEqualTo(450L);

        then = AgeChargeRule.calculateFare(1250L, 17);

        assertThat(then).isEqualTo(720L);


        then = AgeChargeRule.calculateFare(1250L, 19);

        assertThat(then).isEqualTo(1250L);
    }
}
