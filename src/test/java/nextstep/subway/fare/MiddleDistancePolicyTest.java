package nextstep.subway.fare;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.fare.domain.distance.impl.MiddleDistancePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("10km 이상 50km 이하 거리에서의 추가요금에 대한 단위 테스트")
class MiddleDistancePolicyTest {

    @DisplayName("5km 당 추가 요금으로 정상적으로 계산된다")
    @ParameterizedTest
    @ValueSource(ints = {55, 60, 80, 99})
    void calculate_test(int param) {
        MiddleDistancePolicy distancePolicy = MiddleDistancePolicy.getInstance();

        int excepted = (int) ((Math.ceil((param - 1) / 5) + 1) * 100);
        assertThat(distancePolicy.calculate(param)).isEqualTo(excepted);
    }
}
