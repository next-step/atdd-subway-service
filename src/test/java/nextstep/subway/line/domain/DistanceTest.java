package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class DistanceTest {

    @Test
    @DisplayName("역간 거리를 더한다")
    void plus() {
        assertThat(new Distance(5).plus(new Distance(10))).isEqualTo(new Distance(15));
    }

    @Test
    @DisplayName("역간 거리를 뺀다")
    void minus() {
        assertThat(new Distance(10).minus(new Distance(4))).isEqualTo(new Distance(6));
    }

    @Test
    @DisplayName("역간 거리를 뺀 값이 2보다 작으면 예외가 발생한다")
    void minusException() {
        assertThatThrownBy(() -> new Distance(10).minus(new Distance(11)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }
}
