package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DistanceTest {

    @DisplayName("Distance 는 1 이상으로 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 10, 100})
    void create1(int distance) {
        // when & then
        assertThatNoException().isThrownBy(() -> Distance.from(distance));
    }

    @DisplayName("Distance 는 1 보다 작은 값으로 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {-10, -2, -1, 0})
    void create2(int distance) {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Distance.from(distance))
                                            .withMessageContaining("거리의 길이는 1보다 길어야합니다.");
    }

    @DisplayName("Distance(10) 은 10, 15, 20, 100 이하이다.")
    @ParameterizedTest
    @ValueSource(ints = {10, 15, 20, 100})
    void isLessThanOrEqualTo(int value) {
        // given
        Distance distance = Distance.from(10);

        // when
        Distance subDistance = Distance.from(value);

        // then
        assertTrue(distance.isLessThanOrEqualTo(subDistance));
    }

    @DisplayName("Distance(10) 은 10, 5, 1 이상이다.")
    @ParameterizedTest
    @ValueSource(ints = {10, 5, 1})
    void isGreaterThanOrEqualTo(int value) {
        // given
        Distance distance = Distance.from(10);

        // when
        Distance subDistance = Distance.from(value);

        // then
        assertTrue(distance.isGreaterThanOrEqualTo(subDistance));
    }

    @DisplayName("두 Distance 의 거리를 뺀다.")
    @Test
    void minus() {
        // given
        Distance distance10 = Distance.from(10);
        Distance distance15 = Distance.from(15);

        // when
        Distance resultDistance = distance15.minus(distance10);

        // then
        assertThat(resultDistance.getValue()).isEqualTo(5);
    }

    @DisplayName("두 Distance 의 거리를 더한다.")
    @Test
    void plus() {
        // given
        Distance distance10 = Distance.from(10);
        Distance distance15 = Distance.from(15);

        // when
        Distance resultDistance = distance15.plus(distance10);

        // then
        assertThat(resultDistance.getValue()).isEqualTo(25);
    }
}