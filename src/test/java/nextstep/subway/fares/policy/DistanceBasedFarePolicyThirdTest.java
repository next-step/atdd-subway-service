package nextstep.subway.fares.policy;

import nextstep.subway.fares.domain.Fare;
import nextstep.subway.path.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DistanceBasedFarePolicyThirdTest {

    @DisplayName("기본 운임 정책(8km 마다 100원)을 확인한다")
    @Test
    void calculateFareByDistance() {
        // given
        DistanceBasedFarePolicyThird farePolicyThird = new DistanceBasedFarePolicyThird();
        Fare fare = new Fare();
        Distance currDistance = new Distance();
        Distance targetDistance = new Distance(16);

        // when
        farePolicyThird.calculateFareByDistance(currDistance, fare, targetDistance);

        // then
        assertThat(fare.getFare()).isEqualTo(200);
    }
}
