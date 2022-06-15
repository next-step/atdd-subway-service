package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {

    @DisplayName("동등성 비교 하기")
    @Test
    void createTest() {
        assertThat(new Distance(2)).isEqualTo(new Distance(2));
    }

    @DisplayName("음수를 가질수 없다.")
    @Test
    void invalidCreateTest() {
        assertThatThrownBy(() -> new Distance(-1)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("더하면 새로운 Distance 를 반환한다.")
    @Test
    void plusTest() {
        final Distance two = new Distance(2);
        assertThat(two.plus(new Distance(3))).isEqualTo(new Distance(5));
    }

    @DisplayName("빼면 새로운 Distance 를 반환한다.")
    @Test
    void minusTest() {
        assertThat(new Distance(5).minus(new Distance(3))).isEqualTo(new Distance(2));
    }
}