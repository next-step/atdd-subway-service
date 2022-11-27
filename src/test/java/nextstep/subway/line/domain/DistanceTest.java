package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import nextstep.subway.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceTest {
    @Test
    @DisplayName("거리 생성")
    void createDistance() {
        Distance actual = Distance.from(10);
        assertThat(actual).isEqualTo(Distance.from(10));
    }

    @Test
    @DisplayName("거리는 0보다 커야한다")
    void createDistanceGraterThanZero() {
        assertAll(
                () -> assertThat(Distance.from(1)).isInstanceOf(Distance.class),
                () -> assertThrows(InvalidParameterException.class, () -> Distance.from(0)),
                () -> assertThrows(InvalidParameterException.class, () -> Distance.from(-1))
        );
    }

    @Test
    @DisplayName("거리를 더한다")
    void addDistance() {
        // given
        Distance distance = Distance.from(10);
        Distance distance2 = Distance.from(5);

        // when
        Distance actual = distance.add(distance2);

        // then
        assertThat(actual).isEqualTo(Distance.from(15));
    }

    @Test
    @DisplayName("거리를 뺀다")
    void subtractDistance() {
        // given
        Distance distance = Distance.from(10);
        Distance distance2 = Distance.from(5);

        // when
        Distance actual = distance.subtract(distance2);

        // then
        assertThat(actual).isEqualTo(Distance.from(5));
    }
}
