package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DistanceTest {

    @DisplayName("거리는 음수가 아닌 숫자이다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 10, 100})
    void crate(int value) {
        // when
        Distance distance = Distance.valueOf(value);

        // then
        assertThat(distance).isNotNull();
    }

    @DisplayName("길이는 0이거나 음수 값일 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
    void createUnderRange(int value) {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> Distance.valueOf(value));
    }

    @DisplayName("거리 값이 같으면 동등성을 보장한다.")
    @ParameterizedTest
    @ValueSource(ints = {10, 1000})
    void equals(int value) {
        // when
        Distance distance1 = Distance.valueOf(value);
        Distance distance2 = Distance.valueOf(value);

        // then
        assertThat(distance1).isEqualTo(distance2);
    }
}
