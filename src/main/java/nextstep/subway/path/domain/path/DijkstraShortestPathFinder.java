package nextstep.subway.path.domain.path;

import java.util.Objects;
import nextstep.subway.ErrorMessage;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraShortestPathFinder implements PathFinder {

    private final DijkstraShortestPath<Station, SectionEdge> path;
    private final WeightedMultigraph<Station, SectionEdge> graph;

    private DijkstraShortestPathFinder(StationGraph stationGraph) {
        this.graph = stationGraph.getGraph();
        this.path = new DijkstraShortestPath(stationGraph.getGraph());
    }

    public static PathFinder of(StationGraph stationGraph) {
        return new DijkstraShortestPathFinder(stationGraph);
    }

    @Override
    public GraphPath findPath(Station source, Station target) {
        return getGraphPath(source, target);
    }

    private GraphPath getGraphPath(Station source, Station target) {
        validatePathFindRequest(source, target);
        GraphPath graphPath = path.getPath(source, target);
        validatePathFindResult(graphPath);
        return graphPath;
    }

    private void validatePathFindResult(GraphPath graphPath) {
        if (Objects.isNull(graphPath)) {
            throw new IllegalStateException(ErrorMessage.FIND_PATH_NOT_CONNECTED);
        }
    }

    private void validatePathFindRequest(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalStateException(ErrorMessage.FIND_PATH_SAME_STATION);
        }
        if (!graph.containsVertex(source) || !graph.containsVertex(target)) {
            throw new IllegalStateException(ErrorMessage.FIND_PATH_OF_STATION_NOT_ON_GRAPH);
        }
    }

}