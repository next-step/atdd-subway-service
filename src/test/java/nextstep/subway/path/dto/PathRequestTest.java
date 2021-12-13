package nextstep.subway.path.dto;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.exception.SubwayException;

class PathRequestTest {
    @DisplayName("시작점과 출발점이 동일할 때 에러")
    @Test
    void construct_error() {
        assertThatExceptionOfType(SubwayException.class)
            .isThrownBy(() -> new PathRequest(1L, 1L))
            .withMessage("출발역과 도착역이 같습니다.");
    }
}