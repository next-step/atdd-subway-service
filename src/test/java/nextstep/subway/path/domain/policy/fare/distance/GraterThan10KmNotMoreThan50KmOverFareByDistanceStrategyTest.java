package nextstep.subway.path.domain.policy.fare.distance;

import nextstep.subway.path.domain.ShortestDistance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class GraterThan10KmNotMoreThan50KmOverFareByDistanceStrategyTest {
    @DisplayName("추가 요금 계산 획인")
    @ParameterizedTest
    @CsvSource(value = {"11,100", "50,800"})
    public void 추가요금_계산_확인(int distance, int expected) throws Exception {
        //given
        GraterThan10KmNotMoreThan50KmOverFareByDistanceStrategy distanceStrategy = new GraterThan10KmNotMoreThan50KmOverFareByDistanceStrategy();

        //when
        int overFare = distanceStrategy.calculateOverFare(new ShortestDistance(distance));

        //then
        assertThat(overFare).isEqualTo(expected);
    }
}
