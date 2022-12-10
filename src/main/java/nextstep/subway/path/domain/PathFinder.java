package nextstep.subway.path.domain;

import static nextstep.subway.exception.ExceptionMessage.*;

import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.dto.Path;
import nextstep.subway.station.domain.Station;

public class PathFinder {

    private final StationGraph graph;
    private final DijkstraShortestPath path;

    public PathFinder(StationGraph graph) {
        this.graph = graph;
        this.path = new DijkstraShortestPath(graph.getGraph());
    }

    public Path find(Station source, Station target) {
        validateStations(source, target);
        GraphPath graphPath = path.getPath(source, target);
        if (Objects.isNull(graphPath)) {
            throw new IllegalStateException(PATH_NOT_CONNECTED);
        }
        return new Path(graphPath.getVertexList(), new Distance((int)graphPath.getWeight()));
    }

    private void validateStations(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalStateException(PATH_SAME_STATION);
        }

        if (graph.doesNotContain(source) || graph.doesNotContain(target)) {
            throw new IllegalStateException(PATH_MUST_CONTAIN_GRAPH);
        }
    }
}
