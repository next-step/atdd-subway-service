package nextstep.subway.path.domain;


import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {
    @DisplayName("빈 지하철 목록으로 경로 생성 시 예외가 발생한다.")
    @Test
    void createPathWithEmptyStations() {
        assertThatThrownBy(() -> new Path(Collections.emptyList(), 10, new Fare(500)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("경로는 비어있을 수 없습니다.");
    }
}
