package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class OverChargeRuleTest {

    @DisplayName("금액이 맞게 계산되는지 검증")
    @Test
    void calculateOverFare() {
        long charge = OverChargeRule.calculateOverFare(23L);

        assertThat(charge).isEqualTo(500L);
    }

    @DisplayName("거리에 맞게 역할이 분배되는지 검증")
    @Test
    void rule() {
        OverChargeRule rule = OverChargeRule.valueOf(11L);

        assertThat(rule).isEqualTo(OverChargeRule.FROM_11KM_TO_50KM);

        rule = OverChargeRule.valueOf(10L);

        assertThat(rule).isEqualTo(OverChargeRule.FROM_0KM_TO_10KM);

        rule = OverChargeRule.valueOf(51L);

        assertThat(rule).isEqualTo(OverChargeRule.FROM_51KM_OVER);
    }
}
