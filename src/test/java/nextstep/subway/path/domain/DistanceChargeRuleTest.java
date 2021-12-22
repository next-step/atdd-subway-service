package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class DistanceChargeRuleTest {

    @DisplayName("0km 초과 10km 이하일 때 거리 추가 요금 없음")
    @Test
    void FROM_0KM_TO_10KM() {
        assertAll(() -> {
            assertThat(DistanceChargeRule.calculateChargeByDistance(5)).isEqualTo(0);
            assertThat(DistanceChargeRule.calculateChargeByDistance(6)).isEqualTo(0);
            assertThat(DistanceChargeRule.calculateChargeByDistance(7)).isEqualTo(0);
            assertThat(DistanceChargeRule.calculateChargeByDistance(8)).isEqualTo(0);
            assertThat(DistanceChargeRule.calculateChargeByDistance(9)).isEqualTo(0);
            assertThat(DistanceChargeRule.calculateChargeByDistance(10)).isEqualTo(0);
        });
    }

    @DisplayName("10km 초과 50km 이하일 때 추가 요금 계산")
    @Test
    void FROM_10KM_TO_50KM() {
        assertAll(() -> {
            assertThat(DistanceChargeRule.calculateChargeByDistance(20)).isEqualTo(200);
            assertThat(DistanceChargeRule.calculateChargeByDistance(24)).isEqualTo(200);
            assertThat(DistanceChargeRule.calculateChargeByDistance(25)).isEqualTo(300);
            assertThat(DistanceChargeRule.calculateChargeByDistance(27)).isEqualTo(300);
            assertThat(DistanceChargeRule.calculateChargeByDistance(30)).isEqualTo(400);
        });
    }

    @DisplayName("50km 초과일 때 요금 계산")
    @Test
    void OVER_50KM() {
        assertAll(() -> {
            assertThat(DistanceChargeRule.calculateChargeByDistance(50)).isEqualTo(800);
            assertThat(DistanceChargeRule.calculateChargeByDistance(57)).isEqualTo(800);
            assertThat(DistanceChargeRule.calculateChargeByDistance(58)).isEqualTo(900);
            assertThat(DistanceChargeRule.calculateChargeByDistance(63)).isEqualTo(900);
            assertThat(DistanceChargeRule.calculateChargeByDistance(66)).isEqualTo(1000);
        });
    }
}
