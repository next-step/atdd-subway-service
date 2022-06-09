package nextstep.subway.line.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
}
