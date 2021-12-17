package nextstep.subway.path.dto;

import nextstep.subway.common.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathRequestTest {

    @DisplayName("인스턴스 생성 실패 - 출발역과 도착역이 같음")
    @Test
    void instantiate_failure() {
        // given
        long source = 1L;
        long target = 1L;

        // when & then
        assertThatThrownBy(() -> new PathRequest(source, target))
                .hasMessage(ErrorCode.INVALID_PATH.toString());
    }
}
