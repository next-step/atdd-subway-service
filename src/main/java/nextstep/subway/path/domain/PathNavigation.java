package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.List;
import java.util.Objects;

public class PathNavigation {

    public static final int BASIC_FEE = 1250;
    public static final int BASIC_FEE_OVER_50KM = 2050;

    private static final String ERROR_MESSAGE_EQUALS_STATIONS = "동일한 역을 입력하였습니다.";
    private static final String ERROR_MESSAGE_NOT_EXISTED_STATIONS = "존재하지 않은 출발역이나 도착역이 있습니다.";
    private static final String ERROR_MESSAGE_NOT_CONNECTED_STATIONS = "역이 연결되어 있지 않습니다.";

    private final DijkstraShortestPath<Station, SubwayWeightedEdge> path;
    private final SubwayGraph graph;

    private PathNavigation(List<Line> lines) {
        validateNotNull(lines);
        graph = new SubwayGraph(SubwayWeightedEdge.class);
        graph.addVertexWith(lines);
        graph.addEdge(lines);

        path = new DijkstraShortestPath<>(graph);
    }

    public static PathNavigation by(List<Line> lines) {
        return new PathNavigation(lines);
    }


    public Path findShortestPath(Station source, Station target) {
        validateStations(source, target);

        GraphPath<Station, SubwayWeightedEdge> shortestPath = this.path.getPath(source, target);
        validateShortestPathIsNull(shortestPath);
        Integer additionalCharge = shortestPath.getEdgeList().stream().map(SubwayWeightedEdge::getLine)
                .map(Line::getAdditionalCharge)
                .max(Integer::compareTo)
                .orElse(0);

        int distance = (int) shortestPath.getWeight();

        if (distance <= 100) {
            return Path.of(shortestPath.getVertexList(), distance, BASIC_FEE + additionalCharge);
        }

        if (distance <= 500) {
            return Path.of(shortestPath.getVertexList(), distance, BASIC_FEE + calculateOver10KmFare(distance) + additionalCharge);
        }

        return Path.of(shortestPath.getVertexList(), distance, BASIC_FEE_OVER_50KM + calculateOver50KmFare(distance) + additionalCharge);
    }

    private int calculateOver10KmFare(int distance) {
        return (int) ((Math.ceil((distance - 100) / 50) + 1) * 100);
    }

    private int calculateOver50KmFare(int distance) {
        return (int) ((Math.ceil((distance - 500) / 80) + 1) * 100);
    }

    private void validateShortestPathIsNull(GraphPath<Station, SubwayWeightedEdge> shortestPath) {
        if (Objects.isNull(shortestPath)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_CONNECTED_STATIONS);
        }
    }

    private void validateStations(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_EQUALS_STATIONS);
        }

        if (!graph.containsVertex(source) || !graph.containsVertex(target)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_EXISTED_STATIONS);
        }
    }

    private void validateNotNull(List<Line> lines) {
        if (Objects.isNull(lines)) {
            throw new IllegalArgumentException("lines is Null");
        }
    }
}
