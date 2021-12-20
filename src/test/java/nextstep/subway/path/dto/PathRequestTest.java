package nextstep.subway.path.dto;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathRequestTest {
    @DisplayName("시작점과 출발점이 동일할 때 에러")
    @Test
    void construct_error() {
        PathRequest sameRequest = new PathRequest(1L, 1L);
        PathRequest differentRequest = new PathRequest(1L, 2L);

        assertThat(sameRequest.isDifferentSourceAndTarget()).isFalse();
        assertThat(differentRequest.isDifferentSourceAndTarget()).isTrue();
    }
}