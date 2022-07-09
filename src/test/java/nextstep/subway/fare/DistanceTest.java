package nextstep.subway.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {
    @DisplayName("거리를 뺀다")
    @Test
    void minus() {
        Distance distance = new Distance(5);

        assertThat(distance.minus(new Distance(4))).isEqualTo(new Distance(1));
    }

    @DisplayName("기존 거리보다 큰 값은 뺄 수 없다.")
    @Test
    void minus_moreThanExist() {
        Distance distance = new Distance(5);

        assertThatThrownBy(() -> distance.minus(new Distance(6)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
