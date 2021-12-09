package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("구간의 거리")
class DistanceTest {

    @DisplayName("구간의 거리가 최소값보다 작은 경우 예외가 발생한다.")
    @Test
    void validateDistance() {
        // given when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Distance(0));
    }

    @DisplayName("새로운 거리가 기존 구간의 거리가 긴 경우 예외가 발생한다.")
    @Test
    void divisible() {
        // given
        Distance distance = new Distance(5);
        Distance newDistance = new Distance(6);

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> distance.divisible(newDistance));
    }

    @DisplayName("구간에 거리를 더한다.")
    @Test
    void plus() {
        // given
        Distance distance = new Distance(5);
        Distance newDistance = new Distance(3);

        // when
        Distance result = distance.plus(newDistance);

        // then
        assertThat(result.getDistance()).isEqualTo(8);
    }

    @DisplayName("구간에 거리를 뺀다.")
    @Test
    void minus() {
        // given
        Distance distance = new Distance(5);
        Distance newDistance = new Distance(3);

        // when
        Distance result = distance.minus(newDistance);

        // then
        assertThat(result.getDistance()).isEqualTo(2);
    }
}