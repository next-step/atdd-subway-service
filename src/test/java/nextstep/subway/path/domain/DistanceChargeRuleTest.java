package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DistanceChargeRuleTest {

    @DisplayName("금액이 맞게 계산되는지 검증")
    @Test
    void calculateOverFare() {
        long charge = DistanceChargeRule.calculateFare(15L);

        assertThat(charge).isEqualTo(100L);

        charge = DistanceChargeRule.calculateFare(25L);

        assertThat(charge).isEqualTo(300L);

        charge = DistanceChargeRule.calculateFare(10L);

        assertThat(charge).isEqualTo(0L);

        charge = DistanceChargeRule.calculateFare(11L);

        assertThat(charge).isEqualTo(100L);

        charge = DistanceChargeRule.calculateFare(50L);

        assertThat(charge).isEqualTo(800L);

        charge = DistanceChargeRule.calculateFare(58L);

        assertThat(charge).isEqualTo(900L);

        charge = DistanceChargeRule.calculateFare(66L);

        assertThat(charge).isEqualTo(1000L);
    }

    @DisplayName("거리에 맞게 규칙이 분배되는지 검증")
    @Test
    void ruleByDistance() {
        DistanceChargeRule rule = DistanceChargeRule.valueOf(11L);

        assertThat(rule).isEqualTo(DistanceChargeRule.FROM_11KM_TO_50KM);

        rule = DistanceChargeRule.valueOf(10L);

        assertThat(rule).isEqualTo(DistanceChargeRule.FROM_0KM_TO_10KM);

        rule = DistanceChargeRule.valueOf(51L);

        assertThat(rule).isEqualTo(DistanceChargeRule.FROM_51KM_OVER);
    }
}
