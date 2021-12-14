package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class DistanceChargeRuleTest {

    @DisplayName("0 ~ 10km 금액이 맞게 계산되는지 검증")
    @Test
    void from0kmTo10km() {
        long one = DistanceChargeRule.calculateFare(1L);
        long ten = DistanceChargeRule.calculateFare(10L);

        assertAll(
                () -> assertThat(one).isEqualTo(0L),
                () -> assertThat(ten).isEqualTo(0L)
        );
    }

    @DisplayName("11 ~ 50km 금액이 맞게 계산되는지 검증")
    @Test
    void from11kmTo50km() {
        long charge1 = DistanceChargeRule.calculateFare(11L);
        long charge2 = DistanceChargeRule.calculateFare(25L);
        long charge3 = DistanceChargeRule.calculateFare(50L);

        assertAll(
                () -> assertThat(charge1).isEqualTo(100L),
                () -> assertThat(charge2).isEqualTo(300L),
                () -> assertThat(charge3).isEqualTo(800L)
        );
    }

    @DisplayName("51km ~ 금액이 맞게 계산되는지 검증")
    @Test
    void from51kmOver() {
        long charge1 = DistanceChargeRule.calculateFare(58L);
        long charge2 = DistanceChargeRule.calculateFare(66L);

        assertAll(
                () -> assertThat(charge1).isEqualTo(900L),
                () -> assertThat(charge2).isEqualTo(1000L)
        );
    }

    @DisplayName("거리에 맞게 규칙이 분배되는지 검증")
    @Test
    void ruleByDistance() {
        DistanceChargeRule from0kmTo10km = DistanceChargeRule.valueOf(10L);
        DistanceChargeRule from11kmTo50km = DistanceChargeRule.valueOf(11L);
        DistanceChargeRule from51kmOver = DistanceChargeRule.valueOf(51L);

        assertAll(
                () -> assertThat(from0kmTo10km).isEqualTo(DistanceChargeRule.FROM_0KM_TO_10KM),
                () -> assertThat(from11kmTo50km).isEqualTo(DistanceChargeRule.FROM_11KM_TO_50KM),
                () -> assertThat(from51kmOver).isEqualTo(DistanceChargeRule.FROM_51KM_OVER)
        );
    }
}
