package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nextstep.subway.common.exception.InvalidParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceTest {

    private final Integer distanceValue = 100;
    private final Integer overDistanceValue = 101;

    private Distance distance;


    @BeforeEach
    void setUp() {
        // given
        distance = new Distance(distanceValue);
    }

    @Test
    @DisplayName("Distance 길이 감소 검증")
    void minus() {
        // when
        distance.minus(10);

        // then
        assertThat(distance.getDistance()).isEqualTo(distanceValue - 10);
    }

    @Test
    @DisplayName("Distance 길이 보다 같거나 크면 예외 발생함")
    void minus_fail() {
        // then
        assertThrows(InvalidParameterException.class, () -> distance.minus(overDistanceValue));
        assertThrows(InvalidParameterException.class, () -> distance.minus(distanceValue));
    }

    @Test
    @DisplayName("Distance 길이 더하기 검증")
    void plus() {
        // when
        distance.plus(Distance.of(10));

        // then
        assertThat(distance.getDistance()).isEqualTo(distanceValue + 10);
    }
}
