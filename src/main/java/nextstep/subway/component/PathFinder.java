package nextstep.subway.component;

import nextstep.subway.component.domain.SectionWeightedEdge;
import nextstep.subway.component.domain.SubwayGraph;
import nextstep.subway.component.domain.SubwayPath;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PathFinder {
    public SubwayPath shortestPath(List<Line> lines, Optional<Station> source, Optional<Station> target) {
        SubwayGraph subwayGraph = new SubwayGraph(SectionWeightedEdge.class);
        subwayGraph.addVertexesAndEdge(lines);
        return subwayGraph.calcShortestPath(source.get(), target.get());
    }

}
