package nextstep.subway.distance;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.Test;

public class DistanceTest {
    @Test
    void 생성() {
        Distance distance = new Distance(1);
        assertThat(distance.getValue()).isEqualTo(1);
    }
}
