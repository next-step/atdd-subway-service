package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("거리")
public class DistanceTest {

    @DisplayName("거리 생성")
    @ParameterizedTest
    @ValueSource(ints = {1})
    void constructor(int distance) {
        assertThatNoException().isThrownBy(() -> new Distance(distance));
    }

    @DisplayName("거리는 0보다 커야한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void negative(int distance) {
        assertThatThrownBy(() -> new Distance(distance))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("크기를 비교한다. / 크기가 크면 양수를 받환한다.")
    @ParameterizedTest
    @CsvSource({"2, 1, 1"})
    void compare_positive(int target, int source, int result) {
        assertThat(new Distance(target).compareTo(new Distance(source))).isEqualTo(result);
    }

    @DisplayName("크기를 비교한다. / 크기가 작으면 음수를 받환한다.")
    @ParameterizedTest
    @CsvSource({"1, 2, -1"})
    void compare_negative(int target, int source, int result) {
        assertThat(new Distance(target).compareTo(new Distance(source))).isEqualTo(result);
    }

    @DisplayName("크기를 비교한다. / 크기가 같으면 0을 반환한다.")
    @ParameterizedTest
    @CsvSource({"1, 1, 0"})
    void compare_0(int target, int source, int result) {
        assertThat(new Distance(target).compareTo(new Distance(source))).isEqualTo(result);
    }

    @DisplayName("합을 구한다.")
    @Test
    void sum() {

    }

    @DisplayName("차를 구한다.")
    @Test
    void subtract() {

    }
}
