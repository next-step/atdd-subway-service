package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.ExceptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("구간 거리에 대한 단위 테스트")
class DistanceTest {

    @DisplayName("구간의 길이가 0 이하인 값이 오면 예외가 발생해야 한다")
    @ParameterizedTest
    @ValueSource(ints = {-3, -2, -1, 0})
    void distance_length_test(int param) {
        assertThatThrownBy(() -> {
            new Distance(param);
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.MUST_BE_AT_LEAST_LENGTH_ONE.getMessage());
    }

    @DisplayName("길이를 더하면 정상적으로 계산되어야 한다")
    @Test
    void distance_plus_test() {
        // given
        Distance distance = new Distance(5);
        Distance newDistance = new Distance(5);

        // when
        Distance result = distance.plus(newDistance);

        // then
        assertEquals(result.getValue(), 10);
    }

    @DisplayName("길이를 차감하면 정상적으로 차감되어야 한다")
    @Test
    void distance_minus_test() {
        // given
        Distance distance = new Distance(10);
        Distance newDistance = new Distance(5);

        // when
        Distance result = distance.minus(newDistance);

        // then
        assertEquals(result.getValue(), 5);
    }

    @DisplayName("길이를 차감하는데 차감하려는 수가 기존 길이보다 크거나 같으면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {10, 11, 12, 15})
    void distance_minus_exception_test(int param) {
        // given
        Distance distance = new Distance(10);
        Distance newDistance = new Distance(param);

        // then
        assertThatThrownBy(() -> {
            distance.minus(newDistance);
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.IS_NOT_OVER_ORIGIN_DISTANCE.getMessage());
    }
}
