package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.line.exception.IllegalDistanceException;
import org.junit.jupiter.api.Test;

class DistanceTest {

    @Test
    void 차감_테스트() {
        //given
        Distance distance = new Distance(7);

        //when
        Distance newDistance = distance.minus(new Distance(4));

        //then
        assertThat(newDistance).isEqualTo(new Distance(3));
    }

    @Test
    void 증감_테스트() {
        //given
        Distance distance = new Distance(7);

        //when
        Distance newDistance = distance.plus(new Distance(4));

        //then
        assertThat(newDistance).isEqualTo(new Distance(11));
    }

    @Test
    void 차감시_값이_0이하로_내려가면_오류() {
        //given
        Distance distance = new Distance(7);

        //when
        assertThatThrownBy(() -> distance.minus(new Distance(7)))
            .isInstanceOf(IllegalDistanceException.class)
            .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

}