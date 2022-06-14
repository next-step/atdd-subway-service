package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DistanceTest {

    @Test
    void 거리_더하기() {
        Distance distance1 = Distance.from(5);
        Distance distance2 = Distance.from(5);

        assertThat(distance1.sum(distance2)).isEqualTo(Distance.from(10));
    }

    @Test
    void 일_보다_작은_거리_예외() {
        assertThatThrownBy(
                () -> Distance.from(0)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}