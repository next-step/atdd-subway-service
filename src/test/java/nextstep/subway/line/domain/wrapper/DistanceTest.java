package nextstep.subway.line.domain.wrapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class DistanceTest {

    @DisplayName("거리의 최소 1이상 이어야 한다.")
    @Test
    void create() {
        assertAll(
                () -> assertThat(Distance.from(1)).isEqualTo(Distance.from(1)),
                () -> assertThatThrownBy(() -> Distance.from(0)).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> Distance.from(-1)).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("other 거리를 입력하면 거리간의 차이값이 구해진다.")
    @Test
    void distanceDiffWithOtherDistance() {
        // given
        Distance standard = Distance.from(15);
        Distance other = Distance.from(10);
        Distance expected = Distance.from(5);

        // when
        Distance actualDistance = standard.distanceDiffWithOtherDistance(other);

        // then
        assertThat(actualDistance).isEqualTo(expected);
    }
}
