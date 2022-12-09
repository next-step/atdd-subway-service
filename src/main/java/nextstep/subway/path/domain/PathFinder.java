package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;


import java.util.List;

public class PathFinder {

    private static final String IDENTICAL_STATIONS_EXCEPTION = "출발역과 도착역이 동일합니다.";
    private static final String NO_CONNECTION_BETWEEN_STATIONS_EXCEPTION = "출발역과 도착역이 연결되지 않았습니다.";

    private final DijkstraShortestPath dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        Graph graph = new Graph(lines);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph.getGraph());
    }

    private void validatePath(Station source, Station target) {
        if (source == target) {
            throw new InvalidDataException(IDENTICAL_STATIONS_EXCEPTION);
        }
    }

    public List<Station> findShortestPath(Station source, Station target) {
        validatePath(source, target);
        try {
            return dijkstraShortestPath.getPath(source, target).getVertexList();
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException(NO_CONNECTION_BETWEEN_STATIONS_EXCEPTION);
        }
    }

    public Distance findShortestPathDistance(Station source, Station target) {
        validatePath(source, target);
        try {
            return new Distance((int) dijkstraShortestPath.getPath(source, target).getWeight());
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException(NO_CONNECTION_BETWEEN_STATIONS_EXCEPTION);
        }
    }
}
