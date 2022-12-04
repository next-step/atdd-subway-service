package nextstep.subway.path.domain;

import java.util.Objects;
import nextstep.subway.ErrorMessage;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public class DijkstraShortestPathFinder implements PathFinder {

    private final DijkstraShortestPath path;
    private final StationGraph graph;

    public DijkstraShortestPathFinder(StationGraph stationGraph) {
        this.graph = stationGraph;
        this.path = new DijkstraShortestPath(stationGraph.getGraph());
    }

    @Override
    public Path findPath(Station source, Station target) {
        validatePathFindRequest(source, target);
        GraphPath graphPath = path.getPath(source, target);
        validatePathFindResult(graphPath);
        return Path.of(graphPath);
    }
    private void validatePathFindResult(GraphPath graphPath) {
        if (Objects.isNull(graphPath)){
            throw new IllegalStateException(ErrorMessage.FIND_PATH_NOT_CONNECTED);
        }
    }

    private void validatePathFindRequest(Station source, Station target) {
        if (source.equals(target)){
            throw new IllegalStateException(ErrorMessage.FIND_PATH_SAME_STATION);
        }
        if (!graph.containsStation(source) || !graph.containsStation(target)) {
            throw new IllegalStateException(ErrorMessage.FIND_PATH_OF_STATION_NOT_ON_GRAPH);
        }
    }

}