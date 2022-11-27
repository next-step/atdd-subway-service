package nextstep.subway.line.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DistanceTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void 거리가_0이하_값_예외발생(int value) {
        assertThatThrownBy(() -> new Distance(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 거리를_더한다() {
        Distance distance1 = new Distance(10);
        Distance distance2 = new Distance(5);
        Distance expected = new Distance(15);
        assertThat(distance1.add(distance2)).isEqualTo(expected);
    }

    @Test
    void 거리를_뺀다() {
        Distance distance1 = new Distance(10);
        Distance distance2 = new Distance(5);
        Distance expected = new Distance(5);
        assertThat(distance1.subtract(distance2)).isEqualTo(expected);
    }

    @Test
    void 거리를_뺀_결과가_0이하일_경우_예외발생() {
        Distance distance1 = new Distance(10);
        Distance distance2 = new Distance(10);
        assertThatThrownBy(() -> distance1.subtract(distance2))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
