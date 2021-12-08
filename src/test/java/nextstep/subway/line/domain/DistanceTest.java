package nextstep.subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("거리 관련 도메인 기능")
class DistanceTest {
    private Distance distance;

    @BeforeEach
    void setUp() {
       distance = new Distance(5);
    }

    @DisplayName("거리를 더할 수 있다")
    @Test
    void addDistance() {
        final Distance newDistance = distance.add(new Distance(3));

        assertThat(newDistance.getDistance()).isEqualTo(8);
    }

    @DisplayName("거리를 뺄 수 있다")
    @Test
    void subtractDistance() {
        final Distance newDistance = distance.subtract(new Distance(3));

        assertThat(newDistance.getDistance()).isEqualTo(2);
    }

    @DisplayName("거리를 0이하로 뺄 수 없다")
    @Test
    void subtractDistanceException() {
        assertThatThrownBy(() -> {
            final Distance newDistance = distance.subtract(new Distance(5));

        }).isInstanceOf(RuntimeException.class)
        .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }
}