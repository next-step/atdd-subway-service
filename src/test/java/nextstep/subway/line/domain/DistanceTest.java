package nextstep.subway.line.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceTest {

    @Test
    void 거리를_더한다() {
        Distance distance = new Distance(5);
        Distance distance2 = new Distance(3);

        Distance plus = distance.plus(distance2);

        assertThat(plus.getDistance()).isEqualTo(8);
    }

    @Test
    void 거리를_뺀다() {
        Distance distance = new Distance(5);
        Distance distance2 = new Distance(3);

        Distance plus = distance.minus(distance2);

        assertThat(plus.getDistance()).isEqualTo(2);
    }

}
