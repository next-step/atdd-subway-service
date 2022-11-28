package nextstep.subway.distance;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.Test;

public class DistanceTest {
    @Test
    void 생성() {
        Distance distance = Distance.from(1);
        assertThat(distance).isEqualTo(Distance.from(1));
    }

    @Test
    void 거리_더하기() {
        Distance distance1 = Distance.from(10);
        Distance distance2 = Distance.from(10);
        distance1.plusDistance(distance2);
        assertThat(distance1.getDistance()).isEqualTo(20);
    }

    @Test
    void 거리_빼기() {
        Distance distance1 = Distance.from(20);
        Distance distance2 = Distance.from(10);
        distance1.minusDistance(distance2);
        assertThat(distance1.getDistance()).isEqualTo(10);
    }

    @Test
    void 현재_거리보다_더_큰_값인지_검사() {
        Distance distance = Distance.from(10);
        assertThat(distance.isGreaterThan(15)).isTrue();
    }
}
