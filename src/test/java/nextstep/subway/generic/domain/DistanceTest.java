package nextstep.subway.generic.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DistanceTest {

    @Test
    @DisplayName("거리 생성 테스트")
    void create() {
        assertThat(Distance.valueOf(10)).isEqualTo(Distance.valueOf(10));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("거리 생성 실패 테스트")
    void create_exception(int value) {
        assertThatThrownBy(() -> Distance.valueOf(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("거리 비교 테스트 - 작거나 같은 경우")
    void isLessThanOrEqualsTo() {
        assertAll(
            () -> assertThat(Distance.valueOf(10).isLessThanOrEqualsTo(Distance.valueOf(20))).isTrue(),
            () -> assertThat(Distance.valueOf(10).isLessThanOrEqualsTo(Distance.valueOf(10))).isTrue(),
            () -> assertThat(Distance.valueOf(10).isLessThanOrEqualsTo(Distance.valueOf(4))).isFalse()
        );
    }

    @Test
    @DisplayName("거리 빼기 테스트")
    void minus() {
        assertAll(
                () -> assertThat(Distance.valueOf(10).minus(Distance.valueOf(2))).isEqualTo(Distance.valueOf(8)),
                () -> assertThatThrownBy(() -> Distance.valueOf(10).minus(Distance.valueOf(20)))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }
}
