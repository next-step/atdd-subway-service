package nextstep.subway.fare.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("거리 추가 요금 계산")
class DistanceExtraFareTest {
    @Test
    @DisplayName("요금 계산 11km -> 기본 거리에서 1km을 초과하여 추가 요금 없음")
    void noExtraDistanceCharge() {
        int basicDistance = 11;
        int fare = DistanceExtraFare.calculate(basicDistance);

        assertThat(fare).isEqualTo(0);
    }

    @Test
    @DisplayName("요금 계산 15km -> 기본 거리에서 5km을 초과하여 추가 근거리 요금 1회 추가됨")
    void calcDistanceFare3() {
        int basicDistance = 15;
        int fare = DistanceExtraFare.calculate(basicDistance);

        assertThat(fare).isEqualTo(DistanceExtraFare.NEAR.getFare());
    }

    @Test
    @DisplayName("요금 계산 km -> 기본 거리에서 40km을 초과하여 추가 근거리 요금 8회 추가됨")
    void calcDistanceFare4() {
        int basicDistance = 50;
        int fare = DistanceExtraFare.calculate(basicDistance);

        assertThat(fare).isEqualTo(DistanceExtraFare.NEAR.getFare() * 8);
    }

    @Test
    @DisplayName("요금 계산 km -> 기본 거리에서 41km을 초과하여 추가 근거리 요금 8회 추가됨")
    void calcDistanceFare5() {
        int basicDistance = 51;
        int fare = DistanceExtraFare.calculate(basicDistance);

        assertThat(fare).isEqualTo(DistanceExtraFare.NEAR.getFare() * 8);
    }

    @Test
    @DisplayName("요금 계산 km -> 기본 거리에서 48km을 초과하여 추가 근거리 요금 8회 와 원거리 요금 1회 추가됨")
    void calcDistanceFare6() {
        int basicDistance = 58;
        int fare = DistanceExtraFare.calculate(basicDistance);

        assertThat(fare).isEqualTo(DistanceExtraFare.NEAR.getFare() * 8 + DistanceExtraFare.FAR.getFare());
    }

    @Test
    @DisplayName("요금 계산 km -> 기본 거리에서 56km을 초과하여 추가 근거리 요금 8회 와 원거리 요금 2회 추가됨")
    void calcDistanceFare7() {
        int basicDistance = 66;
        int fare = DistanceExtraFare.calculate(basicDistance);

        assertThat(fare).isEqualTo(DistanceExtraFare.NEAR.getFare() * 8 + DistanceExtraFare.FAR.getFare() * 2);
    }
}
