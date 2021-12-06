package nextstep.subway.path.acceptance.domain;


import static nextstep.subway.path.acceptance.domain.PathFixtures.전체구간;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {

    @Test
    @DisplayName("출발역과 도착역이 같은 경우")
    void same_fail() {
        // given
        List<Line> lines = 전체구간();

        // then
        assertThrows(InvalidParameterException.class, () -> Path.of(lines, 1L, 1L));
    }

    @Test
    @DisplayName("출발역,도착역이 없는 경우")
    void notfoundStart() {
        // given
        List<Line> lines = 전체구간();

        // then
        assertThrows(InvalidParameterException.class, () -> Path.of(lines, 100L, 1L));
        assertThrows(InvalidParameterException.class, () -> Path.of(lines, 1L, 100L));
    }

}
