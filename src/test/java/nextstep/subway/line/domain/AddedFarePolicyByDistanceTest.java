package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("거리별 추가 요금 계산")
public class AddedFarePolicyByDistanceTest {

    @DisplayName("거리가 10km 이하인 경우 추가 요금은 0원이다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    void 거리가_10km_이하(int distance) {
        Fare calculate = AddedFarePolicyByDistance.calculate(distance);
        assertThat(calculate.value().intValue()).isEqualTo(1250);
    }

    @DisplayName("거리가 10km 초과, 50km 이하인 경우 5km 마다 100원씩 추가된다.")
    @ParameterizedTest
    @ValueSource(ints = {11, 15})
    void 거리가_10km_초과_50km_이하(int distance) {
        Fare calculate = AddedFarePolicyByDistance.calculate(distance);
        assertThat(calculate.value().intValue()).isEqualTo(1350);
    }

    @DisplayName("거리가 50km 초과인 경우 8km 마다 100원씩 추가된다.")
    @ParameterizedTest
    @ValueSource(ints = {51, 58})
    void 거리가_50km_초과(int distance) {
        Fare calculate = AddedFarePolicyByDistance.calculate(distance);
        assertThat(calculate.value().intValue()).isEqualTo(2150);
    }
}
