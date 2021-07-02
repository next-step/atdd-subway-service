package nextstep.subway.path.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculateUtilsTest {

    @DisplayName("거리당 요금 계산 테스트")
    @Test
    void getIntervalRateOfDistanceTest() {
        // given
        int distance = 38;
        int intervalOfDistance = 5;
        int intervalOfRate = 100;

        // when
        int totalRate = FareCalculateUtils.getTotalFareOfDistance(distance, intervalOfDistance, intervalOfRate);

        // then
        assertThat(totalRate).isEqualTo(800);
    }

    @DisplayName("기본 요금에서 할인률 적용 테스트")
    @Test
    void getFareAfterDiscountTest() {
        // given
        int fare = 2250;
        int baseFare = 350;
        double discountPercent = 0.2;

        // when
        int resultFare = FareCalculateUtils.getFareAfterDiscount(fare, baseFare, discountPercent);

        // then
        assertThat(resultFare).isEqualTo(1520);
    }

}
