package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    @Test
    void compare_positive() {

    }

    @DisplayName("크기를 비교한다. / 크기가 작으면 음수를 받환한다.")
    @Test
    void compare_negative() {

    }

    @DisplayName("크기를 비교한다. / 크기가 같으면 0을 반환한다.")
    @Test
    void compare_0() {

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
