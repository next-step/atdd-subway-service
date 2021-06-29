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
    public static final int LIMIT_BASIC_FEE = 100;
    public static final int LIMIT_ADVANCED_PATH_FEE = 500;
    private static final int ADULT_START_AGE = 20;
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


    public Path findShortestPath(Station source, Station target, int age) {
        validateStations(source, target);

        GraphPath<Station, SubwayWeightedEdge> shortestPath = this.path.getPath(source, target);
        validateShortestPathIsNull(shortestPath);
        int additionalCharge = shortestPath.getEdgeList().stream().map(SubwayWeightedEdge::getLine)
                .map(Line::getAdditionalCharge)
                .max(Integer::compareTo)
                .orElse(0);

        List<Station> Stations = shortestPath.getVertexList();
        int distance = (int) shortestPath.getWeight();

        if (distance <= LIMIT_BASIC_FEE) {
            int fee = BASIC_FEE + additionalCharge;
            return getPathOf(Stations, distance, fee, age);
        }

        if (distance <= LIMIT_ADVANCED_PATH_FEE) {
            int fee = BASIC_FEE + calculateOver10KmFare(distance) + additionalCharge;
            return getPathOf(Stations, distance, fee, age);
        }

        int fee = BASIC_FEE_OVER_50KM + calculateOver50KmFare(distance) + additionalCharge;

        return getPathOf(Stations, distance, fee, age);
    }

    public Path findShortestPath(Station source, Station target) {
        validateStations(source, target);
        return this.findShortestPath(source,target, ADULT_START_AGE);
    }

    private Path getPathOf(List<Station> stations, int distance, int fee, int age) {
        Path path = Path.of(stations, distance, fee);
        path.applyAgeFeeStrategy(age);
        return path;
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
