package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {
    @DisplayName("거리가 0 이하일 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void valueException(int value) {
        assertThatThrownBy(() -> new Distance(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("거리는 0보다 커야 합니다.");
    }

    @DisplayName("거리를 뺄 수 있다")
    @Test
    void subtract() {
        // given
        Distance distance1 = new Distance(10);
        Distance distance2 = new Distance(4);

        // when
        Distance result = distance1.subtract(distance2);

        // then
        assertThat(result.get()).isEqualTo(6);
    }

    @DisplayName("거리를 더할 수 있다")
    @Test
    void add() {
        // given
        Distance distance1 = new Distance(10);
        Distance distance2 = new Distance(40);

        // when
        Distance result = distance1.add(distance2);

        // then
        assertThat(result.get()).isEqualTo(50);
    }
}
