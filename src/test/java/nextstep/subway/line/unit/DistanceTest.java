package nextstep.subway.line.unit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DistanceTest {

    @DisplayName("거리를 늘릴 수 있다.")
    @Test
    void addDistance_test() {
        // given
        Distance 거리 = Distance.from(10);
        Distance 추가_거리 = Distance.from(5);

        // when
        Distance 새로운_거리 = 거리.add(추가_거리);

        // then
        assertEquals(15, 새로운_거리.value());
    }

    @DisplayName("거리를 줄일 수 있다.")
    @Test
    void subtractDistance_test() {
        // given
        Distance 거리 = Distance.from(10);
        Distance 차감_거리 = Distance.from(5);
        // when
        Distance 새로운_거리 = 거리.subtract(차감_거리);

        // then
        assertEquals(5, 새로운_거리.value());
    }

    @DisplayName("거리를 생성할때 거리가 0과 같거나 음수라면 IllegalArgumentException 발생합니다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void createDistance_exception(int 거리) {

        // then
        assertThatThrownBy(() -> Distance.from(거리)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("거리를 줄일때 줄이는 거리가 현재 거리보다 같거나 크면 IllegalStateException 발생합니다.")
    @ParameterizedTest
    @ValueSource(ints = {10, 11})
    void subtractDistance_exception(int 차감) {
        // given
        Distance 거리 = Distance.from(10);
        Distance 차감_거리 = Distance.from(차감);

        // then
        assertThatThrownBy(() -> 거리.subtract(차감_거리)).isInstanceOf(IllegalStateException.class);
    }

}
