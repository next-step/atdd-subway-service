package nextstep.subway.line.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 구간의_길이는_0보다_커야한다(int distance) {
        // when & then
        assertThatThrownBy(() ->
                new Distance(distance)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 구간의_길이를_뺀다() {
        // given
        Distance target = new Distance(10);
        Distance distance = new Distance(7);

        // when
        Distance result = target.minus(distance);

        // then
        assertThat(result.value()).isEqualTo(3);
    }

    @Test
    void 구간의_길이를_더한다() {
        // given
        Distance target = new Distance(10);
        Distance distance = new Distance(10);

        // when
        Distance result = target.plus(distance);

        // then
        assertThat(result.value()).isEqualTo(20);
    }
}
