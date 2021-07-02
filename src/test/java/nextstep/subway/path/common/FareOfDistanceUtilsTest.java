package nextstep.subway.path.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FareOfDistanceUtilsTest {

    @DisplayName("거리당 요금 계산 테스트")
    @Test
    void getIntervalRateOfDistanceTest() {
        int distance = 38;
        int intervalOfDistance = 5;
        int intervalOfRate = 100;

        // when
        int totalRate = FareOfDistanceUtils.getIntervalRateOfDistance(distance, intervalOfDistance, intervalOfRate);

        // then
        assertThat(totalRate).isEqualTo(800);
    }

}
