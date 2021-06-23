package nextstep.subway.line.domain;

import nextstep.subway.wrapped.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DistanceTest {
    @Test
    @DisplayName("거리는 음수이면 IllegalArgumentException이 발생한다")
    void 거리는_음수이면_IllegalArgumentException이_발생한다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Distance(-1));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @DisplayName("거리가 양수이면 생성이 된다")
    void 거리가_양수이면_생성이_된다(int distance) {
        assertDoesNotThrow(() -> new Distance(distance));
    }

    @Test
    @DisplayName("같은 값의 거리는 hashcode와 equals가 같다")
    void 같은_값의_거리는_hashcode와_equals가_같다() {
        Distance distance1 = new Distance(1);
        Distance distance2 = new Distance(1);
        Distance distance3 = new Distance(3);

        assertThat(distance1.hashCode())
                .isEqualTo(distance2.hashCode());
        assertThat(distance1)
                .isEqualTo(distance2);

        assertThat(distance2.hashCode())
                .isNotEqualTo(distance3.hashCode());
        assertThat(distance2)
                .isNotEqualTo(distance3);
    }
}