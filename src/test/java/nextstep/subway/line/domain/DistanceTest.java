package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceTest {

    @Test
    @DisplayName("Distance의 정상 생성을 확인한다.")
    void createDistance() {

        Distance distance = new Distance(10);
        assertThat(distance).isEqualTo(new Distance(10));
    }

    @Test
    @DisplayName("Distance에 양수가 아닌 값이 들어올 경우 에러를 던진다.")
    void createDistance_fail() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new Distance(0));
    }

    @Test
    @DisplayName("Distance의 거리에서 특정 거리를 뺄 경우 새로운 객체를 리턴한다.")
    void subtractDistance() {
        Distance subtract = new Distance(10).subtract(5);
        assertThat(subtract).isEqualTo(new Distance(5));
    }

}
