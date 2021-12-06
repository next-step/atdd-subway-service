package nextstep.subway.path.acceptance.infrastructure;

import static nextstep.subway.path.acceptance.domain.PathFixtures.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathEdge;
import nextstep.subway.path.infrastructure.JgraphtPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JgraphtPathTest {

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    void getShortestPath3() {
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
