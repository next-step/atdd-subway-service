package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.auth.fare.domain.SubwayFareCalculator;
import nextstep.subway.common.exception.ErrorEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class SubwayFareCalculatorTest {

    @ParameterizedTest()
    @ValueSource(ints = {1, 5, 10})
    void 거리_10km이하_입력시_기본_운임요금_반환(int distance) {
        int fare = SubwayFareCalculator.calculate(distance);

        assertThat(fare).isEqualTo(SubwayFareCalculator.BASIC_FARE);
    }

    @Test
    void 거리_1km이하_유효하지않는_경우_예외_발생() {
        assertThatThrownBy(() -> SubwayFareCalculator.calculate(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith(ErrorEnum.DISTANCE_GREATER_ZERO.message());
    }
}
