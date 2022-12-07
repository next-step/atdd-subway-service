package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ShortestPathTest {

    @DisplayName("경로값이 없으면 예외발생")
    @Test
    void returnsExceptionWithNullPath() {
        assertThatThrownBy(() -> new ShortestPath(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("경로가 존재해야 합니다");
    }


}
