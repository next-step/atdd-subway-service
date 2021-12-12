package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OverChargeRuleTest {

    @DisplayName("금액이 맞게 계산되는지 검증")
    @Test
    void calculateOverFare() {
        long charge = OverChargeRule.calculateByZone(15L);

        assertThat(charge).isEqualTo(100L);

        charge = OverChargeRule.calculateByZone(25L);

        assertThat(charge).isEqualTo(300L);

        charge = OverChargeRule.calculateByZone(10L);

        assertThat(charge).isEqualTo(0L);

        charge = OverChargeRule.calculateByZone(11L);

        assertThat(charge).isEqualTo(100L);

        charge = OverChargeRule.calculateByZone(50L);

        assertThat(charge).isEqualTo(800L);

        charge = OverChargeRule.calculateByZone(58L);

        assertThat(charge).isEqualTo(900L);

        charge = OverChargeRule.calculateByZone(66L);

        assertThat(charge).isEqualTo(1000L);
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
