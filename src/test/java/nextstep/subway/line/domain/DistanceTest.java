package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import nextstep.subway.line.exception.DistanceTooLongException;
import nextstep.subway.line.exception.InvalidDistanceException;
import org.junit.jupiter.api.Test;

class DistanceTest {

    @Test
    void test_최소_거리_보다_짧으면_예외_발생() {
        int wrongDistance = 0;
        assertAll(
            () -> assertThat(wrongDistance).isLessThan(Distance.MINIMUM),
            () -> assertThatThrownBy(() -> Distance.of(wrongDistance))
                .isInstanceOf(InvalidDistanceException.class)
        );
    }

    @Test
    void test_더_멀거나_같은_거리를_빼려고_하면_예외_발생() {
        Distance distance = Distance.of(3);

        assertAll(
            () -> assertThatThrownBy(() -> distance.subtract(Distance.of(4)))
                .isInstanceOf(DistanceTooLongException.class),
            () -> assertThatThrownBy(() -> distance.subtract(Distance.of(3)))
                .isInstanceOf(DistanceTooLongException.class)
        );
    }

    @Test
    void test_거리_빼기() {
        Distance distance = Distance.of(5);

        assertThat(distance.subtract(Distance.of(3)).getDistance()).isEqualTo(2);
    }

    @Test
    void test_거리_더하기() {
        Distance distance = Distance.of(1);

        assertThat(distance.add(Distance.of(7)).getDistance()).isEqualTo(8);
    }
}