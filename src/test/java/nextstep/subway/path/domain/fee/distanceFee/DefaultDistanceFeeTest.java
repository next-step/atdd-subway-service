package nextstep.subway.path.domain.fee.distanceFee;

import nextstep.subway.path.domain.exceptions.InvalidFeeDistanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultDistanceFeeTest {
    @DisplayName("0 ~ 10km 내의 범위로 오브젝트를 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = { 0, 10 })
    void createTest(Integer distance) {
        DistanceFee distanceFee = new DefaultDistanceFee(distance);

        assertThat(distanceFee).isNotNull();
    }

    @DisplayName("0 ~ 10km를 벗어난 범위로 오브젝트를 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = { -1, 11 })
    void createFailTest(Integer invalidDistance) {
        assertThatThrownBy(() -> new DefaultDistanceFee(invalidDistance))
                .isInstanceOf(InvalidFeeDistanceException.class)
                .hasMessage("기본 요금은 0km 이상 ~ 10km 이하여야 합니다.");
    }
}