package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {

    @DisplayName("구간 거리가 0보다 작을 시 예외를 발생시킨다.")
    @Test
    void createException() {
        //when
        assertThatThrownBy(() -> Distance.valueOf(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Distance.INVALID_DISTANCE_EXCEPTION_MESSAGE);
    }

    @DisplayName("구간의 거리를 뺀다.")
    @Test
    void minus() {
        //given
        Distance originalDistance = Distance.valueOf(10);
        Distance distanceToMinus = Distance.valueOf(3);

        //when
        Distance actual = originalDistance.minus(distanceToMinus);

        //then
        assertThat(actual.getValue()).isEqualTo(7);
    }

    @DisplayName("역과 역 사이의 거리보다 좁은 거리를 입력하면 예외를 발생시킨다.")
    @Test
    void minusBiggerThanOriginalDistanceException() {
        //given
        Distance originalDistance = Distance.valueOf(10);
        Distance distanceToMinus = Distance.valueOf(11);

        //when
        assertThatThrownBy(() -> originalDistance.minus(distanceToMinus))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Distance.BIGGER_THAN_DISTANCE_EXCEPTION_MESSAGE);
    }

    @DisplayName("구간의 거리를 더한다.")
    @Test
    void plus() {
        //given
        Distance originalDistance = Distance.valueOf(10);
        Distance distanceToPlus = Distance.valueOf(3);

        //when
        Distance actual = originalDistance.plus(distanceToPlus);

        //then
        assertThat(actual.getValue()).isEqualTo(13);
    }
}
