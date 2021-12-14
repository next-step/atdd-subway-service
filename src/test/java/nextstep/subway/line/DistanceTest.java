package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.line.domain.Distance;

@DataJpaTest
public class DistanceTest {
    @DisplayName("거리 생성")
    @Test
    void init() {
        //given
        Distance distance = new Distance(10);

        //when
        int result = distance.getDistance();

        //then
        assertThat(result).isEqualTo(10);
    }

    @DisplayName("거리 빼기")
    @Test
    void minus() {
        //given
        Distance distance = new Distance(10);
        Distance minusDistance = new Distance(3);

        //when
        int result = distance.minus(minusDistance).getDistance();

        //then
        assertThat(result).isEqualTo(7);
    }

    @DisplayName("역과 역 사이의 거리보다 좁은 거리를 입력해주세요")
    @Test
    void invalidMinus() {

        assertThatIllegalArgumentException()
            .isThrownBy(() -> {
                //given
                Distance distance = new Distance(10);
                Distance minusDistance = new Distance(30);

                //when
                distance.minus(minusDistance);
            }).withMessageMatching("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("거리 더하기")
    @Test
    void plus() {
        //given
        Distance distance = new Distance(10);
        Distance plusDistance = new Distance(5);

        //when
        int count = distance.plus(plusDistance).getDistance();

        //then
        assertThat(count).isEqualTo(15);
    }
}
