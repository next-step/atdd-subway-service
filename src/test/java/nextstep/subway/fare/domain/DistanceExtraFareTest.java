package nextstep.subway.fare.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceExtraFareTest {
    @Test
    void calcDistanceFare2() {
        int basicDistance = 11;
        int fare = DistanceExtraFare.calculate(basicDistance);

        assertThat(fare).isEqualTo(0);
    }

    @Test
    void calcDistanceFare3() {
        int basicDistance = 15;
        int fare = DistanceExtraFare.calculate(basicDistance);

        assertThat(fare).isEqualTo(DistanceExtraFare.NEAR.getFare());
    }

    @Test
    void calcDistanceFare4() {
        int basicDistance = 50;
        int fare = DistanceExtraFare.calculate(basicDistance);

        assertThat(fare).isEqualTo(DistanceExtraFare.NEAR.getFare() * 8);
    }

    @Test
    void calcDistanceFare5() {
        int basicDistance = 51;
        int fare = DistanceExtraFare.calculate(basicDistance);

        assertThat(fare).isEqualTo(DistanceExtraFare.NEAR.getFare() * 8);
    }

    @Test
    void calcDistanceFare6() {
        int basicDistance = 58;
        int fare = DistanceExtraFare.calculate(basicDistance);

        assertThat(fare).isEqualTo(DistanceExtraFare.NEAR.getFare() * 8 + DistanceExtraFare.FAR.getFare());
    }

    @Test
    void calcDistanceFare7() {
        int basicDistance = 66;
        int fare = DistanceExtraFare.calculate(basicDistance);

        assertThat(fare).isEqualTo(DistanceExtraFare.NEAR.getFare() * 8 + DistanceExtraFare.FAR.getFare() * 2);
    }
}
