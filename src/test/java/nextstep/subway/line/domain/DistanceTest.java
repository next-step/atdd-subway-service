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
    @DisplayName("거리가 짧거나 같은지 비교한다.")
    void 거리가_짧거나_같은지_비교() {
        Distance distance = new Distance(10);
        assertThat(distance.isShortOrEqualTo(new Distance(9))).isFalse();
        assertThat(distance.isShortOrEqualTo(new Distance(10))).isTrue();
        assertThat(distance.isShortOrEqualTo(new Distance(11))).isTrue();
    }

    @Test
    @DisplayName("거리를 뺀다.")
    void 거리_빼기() {
        Distance distance = new Distance(10);
        distance.minus(new Distance(3));
        assertThat(distance).isEqualTo(new Distance(7));
    }
}
