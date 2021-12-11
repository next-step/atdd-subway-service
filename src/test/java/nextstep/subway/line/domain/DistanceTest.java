package nextstep.subway.line.domain;

import nextstep.subway.line.application.exception.InvalidSectionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("구간의 거리")
class DistanceTest {

    @DisplayName("구간 거리가 최소값보다 작은 경우 예외가 발생한다.")
    @Test
    void validateDistance() {
        // given when then
        assertThatThrownBy(() -> new Distance(0))
                .isInstanceOf(InvalidSectionException.class);
    }

    @DisplayName("새로운 거리가 기존 구간의 거리가 긴 경우 예외가 발생한다.")
    @Test
    void divisible() {
        // given
        Distance distance = new Distance(5);
        int newDistance = 6;

        // when then
        assertThatThrownBy(() -> distance.divisible(newDistance))
                .isInstanceOf(InvalidSectionException.class);
    }

    @DisplayName("구간에 거리를 더한다.")
    @Test
    void plus() {
        // given
        Distance distance = new Distance(5);

        // when
        Distance result = distance.plus(3);

        // then
        assertThat(result.getDistance()).isEqualTo(8);
    }

    @DisplayName("구간에 거리를 뺀다.")
    @Test
    void minus() {
        // given
        Distance distance = new Distance(5);

        // when
        Distance result = distance.minus(3);

        // then
        assertThat(result.getDistance()).isEqualTo(2);
    }
}