package nextstep.subway.fares.policy;

import nextstep.subway.fares.domain.Fare;
import nextstep.subway.path.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DistanceBasedFarePolicyFirstTest {

    @DisplayName("기본 운임 정책(10km 이내)을 확인한다")
    @Test
    void calculateFareByDistance() {
        // given
        DistanceBasedFarePolicyFirst farePolicyFirst = new DistanceBasedFarePolicyFirst();
        Fare fare = new Fare();
        Distance currDistance = new Distance();
        Distance targetDistance = new Distance(10);

        // when
        farePolicyFirst.calculateFareByDistance(currDistance, fare, targetDistance);

        // then
        assertThat(fare.getFare()).isEqualTo(1250);
    }
}
