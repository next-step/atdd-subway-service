package nextstep.subway.path.infrastructure;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.application.PathSearch;
import nextstep.subway.path.dto.PathResultV2;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

@Component
public class PathSearchImpl implements PathSearch {

    @Override
    public PathResultV2 findShortestPath(List<Line> lines, Station source, Station target) {
        SubwayGraph subwayGraph = new SubwayGraph(getSectionEdges(lines));
        SubwayPath subwayPath = new SubwayPath(subwayGraph);
        return subwayPath.getShortestPath(source, target);
    }

    private List<SectionEdge> getSectionEdges(List<Line> lines) {
        return lines.stream()
            .map(Line::getSections)
            .flatMap(Collection::stream)
            .map(SectionEdge::of)
            .collect(Collectors.toList());
    }
}
