package nextstep.subway.path.domain;

import static nextstep.subway.path.step.PathFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {

    @Test
    @DisplayName("출발역과 도착역이 같은 경우")
    void 출발역_도착역_같은경우_실패() {
        // given
        Lines lines = new Lines(전체구간());

        // then
        assertThrows(InvalidParameterException.class, () -> lines.toPath(교대, 교대));
    }

    @Test
    @DisplayName("출발역,도착역이 없는 경우")
    void 출발역_도착역_없는경우_실패() {
        // given
        Lines lines = new Lines(전체구간());

        // then
        assertThrows(InvalidParameterException.class, () -> lines.toPath(없는역, 교대));
        assertThrows(InvalidParameterException.class, () -> lines.toPath(교대, 없는역));
    }


    @Test
    @DisplayName("경로 조회 노선 추가요금 책정")
    void lineAdditionalFare() {
        // when
        Lines lines = new Lines(전체구간());
        Path path = lines.toPath(강남, 남부터미널);

        assertThat(path.getMaxLineAdditionalFare()).isEqualTo(이호선추가요금);
    }
}
