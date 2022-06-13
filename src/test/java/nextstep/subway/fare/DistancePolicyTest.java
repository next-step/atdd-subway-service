package nextstep.subway.fare;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.fare.domain.distance.DistanceFareType;
import nextstep.subway.fare.domain.distance.DistancePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("거리에 따른 추가 운임요금 계산 단위 테스트")
class DistancePolicyTest {

    @DisplayName("10km 이내의 기본거리에 대해서는 추가요금은 0원이어야 한다")
    @ParameterizedTest
    @ValueSource(ints = {2,3,6,9})
    void basic_distance_test(int param) {
        DistancePolicy distancePolicy = DistanceFareType.getDistancePolicy(param);

        assertThat(distancePolicy.calculate(param)).isEqualTo(0);
    }

    @DisplayName("10km 이상 50km 이하의 거리에 대해서는 추가요금은 5km 단위당 100원이어야 한다")
    @ParameterizedTest
    @ValueSource(ints = {25, 30, 40, 47})
    void middle_distance_test(int param) {
        DistancePolicy distancePolicy = DistanceFareType.getDistancePolicy(param);

        int expected = (int) ((Math.ceil((param - 1) / 5) + 1) * 100);
        assertThat(distancePolicy.calculate(param)).isEqualTo(expected);
    }

    @DisplayName("10km 이상 50km 이하의 거리에 대해서는 추가요금은 5km 단위당 100원이어야 한다")
    @ParameterizedTest
    @ValueSource(ints = {51, 88, 160, 222})
    void long_distance_test(int param) {
        DistancePolicy distancePolicy = DistanceFareType.getDistancePolicy(param);

        int expected = (int) ((Math.ceil((param - 1) / 8) + 1) * 100);
        assertThat(distancePolicy.calculate(param)).isEqualTo(expected);
    }
}
