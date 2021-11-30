package nextstep.subway.path.application;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResult;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

@Component
public class PathFinder {

    private final Graph graph;

    public PathFinder(Graph graph) {
        this.graph = graph;
    }

    public PathResult findShortestPath(Sections sections, Station source, Station target) {
        Path path = graph.getPath(sections);
        return path.find(source, target);
    }
}
