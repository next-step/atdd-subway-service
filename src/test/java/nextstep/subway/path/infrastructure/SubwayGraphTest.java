package nextstep.subway.path.infrastructure;

import static nextstep.subway.path.domain.PathFixtures.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class SubwayGraphTest {

    private SubwayGraph subwayGraph;

    @BeforeEach
    void setUp() {
        // given
        List<SectionEdge> sectionEdges = getSectionEdges();
        subwayGraph = new SubwayGraph(sectionEdges);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    void 출발역_도착역_연결되어_있지_않음_실패() {
        // when
        SubwayPath subwayPath = new SubwayPath(subwayGraph);

        // then
        assertThrows(InvalidParameterException.class, () -> subwayPath.getShortestPath(강남, 천호));
    }

    private List<SectionEdge> getSectionEdges() {
        List<Line> lines = 전체구간();
        return lines.stream()
            .map(Line::getSections)
            .flatMap(Collection::stream)
            .map(SectionEdge::of)
            .collect(Collectors.toList());
    }
}