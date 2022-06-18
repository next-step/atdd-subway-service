package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DistanceTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, -2})
    @DisplayName("거리가 음수이면 Distance 생성시 예외가 발생해야 한다.")
    void createDistance_exception(int value) {
        assertThatThrownBy(() -> Distance.from(value))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("구간 길이의 합을 반환한다.")
    void addDistance() {
        // given
        Distance saved = Distance.from(10);

        // when
        Distance added = saved.add(Distance.from(5));

        // then
        assertThat(added.value()).isEqualTo(15);
    }

    @Test
    @DisplayName("구간 길이의 차를 반환한다.")
    void subtractDistance() {
        // given
        Distance saved = Distance.from(10);

        // when
        Distance subtracted = saved.subtract(Distance.from(5));

        // then
        assertThat(subtracted.value()).isEqualTo(5);
    }

    @Test
    @DisplayName("구간 길이의 차가 양수가 아닐 경우 예외가 발생한다.")
    void subtractDistance_exception() {
        // given
        Distance saved = Distance.from(10);

        // then
        assertAll(
            () -> assertThatThrownBy(() -> saved.subtract(Distance.from(10)))
                .isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> saved.subtract(Distance.from(20)))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }
}
