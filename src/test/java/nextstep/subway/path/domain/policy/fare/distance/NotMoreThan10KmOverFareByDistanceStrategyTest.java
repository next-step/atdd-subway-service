package nextstep.subway.path.domain.policy.fare.distance;

import nextstep.subway.path.domain.ShortestDistance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class NotMoreThan10KmOverFareByDistanceStrategyTest {
    @DisplayName("추가 요금 계산 획인")
    @ParameterizedTest
    @ValueSource(ints = {0, 10})
    public void 추가요금_계산_확인(int distance) throws Exception {
        //given
        NotMoreThan10KmOverFareByDistanceStrategy distanceStrategy = new NotMoreThan10KmOverFareByDistanceStrategy();

        //when
        int overFare = distanceStrategy.calculateOverFare(new ShortestDistance(distance));

        //then
        assertThat(overFare).isEqualTo(0);
    }
}
