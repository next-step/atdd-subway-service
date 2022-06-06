package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Distance 클래스 테스트")
class DistanceTest {

    @DisplayName("다른 Distance로 뺀다")
    @Test
    void successfulMinus() {
        Distance distance = new Distance(10);
        Distance other = new Distance(5);

        distance.minus(other);

        assertThat(distance).isEqualTo(new Distance(5));
    }

    @DisplayName("다른 Distance를 뺄 때 0보다 작을 수  없다")
    @Test
    void failureMinus() {
        Distance distance = new Distance(10);
        Distance other = new Distance(11);

        assertThatThrownBy(() -> {
            distance.minus(other);
        }).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }
}
