package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

class DistanceTest {
    @Test
    @DisplayName("거리는 음수이면 IllegalArgumentException이 발생한다")
    void 거리는_음수이면_IllegalArgumentException이_발생한다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Distance(-1));
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5})
    @DisplayName("거리가 양수이면 생성이 된다")
    void 거리가_양수이면_생성이_된다(int distance) {
        assertDoesNotThrow(() -> new Distance(distance));
    }
}