package nextstep.subway.path.infrastructure;

import static nextstep.subway.path.domain.PathFixtures.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathEdge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JgraphtPathTest {

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    void 출발역_도착역_연결되어_있지_않음_실패() {
        // given
        Path path = new Path(전체구간(), 교대.getId(), 천호.getId());
        List<PathEdge> pathEdges = path.toPathEdges();
        Long source = path.getSource();
        Long target = path.getTarget();

        // when
        JgraphtPath jgraphtPath = new JgraphtPath();

        // then
        assertThrows(InvalidParameterException.class,
            () -> jgraphtPath.findShortestPath(pathEdges, source, target));
    }
}
