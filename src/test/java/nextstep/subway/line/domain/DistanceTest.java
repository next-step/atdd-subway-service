package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceTest {
    @Test
    @DisplayName("거리를 생성한다.")
    void 거리_생성() {
        Distance distance = new Distance(10);
        assertThat(distance).isNotNull();
        assertThat(distance.getDistance()).isEqualTo(10);
    }

    @Test
    @DisplayName("거리를 비교한다.")
    void 거리_비교() {
        Distance distance = new Distance(10);
        assertThat(distance).isEqualTo(new Distance(10));
        assertThat(distance).isNotEqualTo(new Distance(20));
    }

    @Test
    @DisplayName("거리를 뺀다.")
    void 거리_빼기() {
        Distance distance = new Distance(10);
        distance = Distance.subtract(distance, new Distance(3));
        assertThat(distance).isEqualTo(new Distance(7));
    }
}
