package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class DistanceTest {

    @DisplayName("거리 생성 시 0 미만의 값 예외 발생")
    @Test
    void testCreateDistanceWithNegativeValue() {
        // given
        int distance = -1;

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> new Distance(distance));
    }
}
