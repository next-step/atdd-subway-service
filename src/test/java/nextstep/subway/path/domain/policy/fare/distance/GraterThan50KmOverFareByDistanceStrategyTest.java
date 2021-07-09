package nextstep.subway.path.domain.policy.fare.distance;

import nextstep.subway.path.domain.ShortestDistance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GraterThan50KmOverFareByDistanceStrategyTest {
    @DisplayName("추가 요금 계산 획인")
    @Test
    public void 추가요금_계산_확인() throws Exception {
        //given
        GraterThan50KmOverFareByDistanceStrategy distanceStrategy = new GraterThan50KmOverFareByDistanceStrategy();

        //when
        int overFare = distanceStrategy.calculateOverFare(new ShortestDistance(51));

        //then
        assertThat(overFare).isEqualTo(900);
    }
}
