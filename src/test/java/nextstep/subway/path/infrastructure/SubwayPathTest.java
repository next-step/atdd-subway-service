package nextstep.subway.path.infrastructure;

import static nextstep.subway.path.domain.PathFixtures.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubwayPathTest {

    @Test
    @DisplayName("최단경로 단위테스트")
    void 최단경로_조회() {
        List<Line> lines = 전체구간();

        List<SectionEdge> sectionEdges = lines.stream()
            .map(Line::getSections)
            .flatMap(Collection::stream)
            .map(SectionEdge::of)
            .collect(Collectors.toList());
    }
}
