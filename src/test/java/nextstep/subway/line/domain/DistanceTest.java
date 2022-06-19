package nextstep.subway.line.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceTest {

    @Test
    void 거리_빼기_계산() {
        // given
        Distance distance1 = new Distance(5);
        Distance distance2 = new Distance(3);

        // when
        Distance expected = new Distance(distance1.getValue() - distance2.getValue());
        distance1.minus(distance2);

        // then
        assertThat(distance1).isEqualTo(expected);
    }

    @Test
    void 거리_더하기_계산() {
        // given
        Distance distance1 = new Distance(5);
        Distance distance2 = new Distance(3);

        // when
        Distance expected = new Distance(distance1.getValue() + distance2.getValue());
        Distance actual = Distance.sum(distance1, distance2);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}