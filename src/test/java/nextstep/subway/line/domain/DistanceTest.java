package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DistanceTest {
    @DisplayName("거리가 0 이하일 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void 거리는_0_이하일_수_없다(int value) {
        assertThatThrownBy(() -> new Distance(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 0이하의 값일 수 없습니다.");
    }

    @DisplayName("동등성 비교")
    @Test
    void equals() {
        assertThat(new Distance(5)).isEqualTo(new Distance(5));
    }

    @DisplayName("거리를 더 할 수 있다.")
    @Test
    void plus() {
        Distance distance = new Distance(7);
        Distance anotherDistance = new Distance(3);
        Distance expected = new Distance(10);

        assertThat(distance.add(anotherDistance)).isEqualTo(expected);
    }

    @DisplayName("거리를 뺄 수 있다.")
    @Test
    void subtract() {
        Distance distance = new Distance(10);
        Distance anotherDistance = new Distance(3);
        Distance expected = new Distance(7);

        assertThat(distance.subtract(anotherDistance)).isEqualTo(expected);
    }

    @DisplayName("거리를 뺀 결과가 0 이하일 경우 예외가 발생한다.")
    @Test
    void subtractException() {
        Distance distance = new Distance(3);
        Distance anotherDistance = new Distance(7);
        assertThatThrownBy(() -> distance.subtract(anotherDistance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 0이하의 값일 수 없습니다.");
    }

    @DisplayName("거리의 크기를 비교할 수 있다.")
    @Test
    void isOver() {
        Distance distance = new Distance(7);
        Distance anotherDistance = new Distance(5);

        assertThat(distance.isOver(anotherDistance)).isTrue();
    }
}
