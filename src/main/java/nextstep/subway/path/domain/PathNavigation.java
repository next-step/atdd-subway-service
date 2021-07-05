package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.List;
import java.util.Objects;

public class PathNavigation {

    private static final int ADULT_START_AGE = 20;
    private static final String ERROR_MESSAGE_EQUALS_STATIONS = "동일한 역을 입력하였습니다.";
    private static final String ERROR_MESSAGE_NOT_EXISTED_STATIONS = "존재하지 않은 출발역이나 도착역이 있습니다.";
    private static final String ERROR_MESSAGE_NOT_CONNECTED_STATIONS = "역이 연결되어 있지 않습니다.";

    private final DijkstraShortestPath<Station, SubwayWeightedEdge> shortestPath;
    private final SubwayGraph graph;

    private PathNavigation(List<Line> lines) {
        validateNotNull(lines);
        graph = new SubwayGraph(SubwayWeightedEdge.class);
        graph.addVertexWith(lines);
        graph.addEdge(lines);

        shortestPath = new DijkstraShortestPath<>(graph);
    }

    public static PathNavigation by(List<Line> lines) {
        return new PathNavigation(lines);
    }


    public Path findShortestPath(Station source, Station target, int age) {
        validateStations(source, target);

        GraphPath<Station, SubwayWeightedEdge> path = this.shortestPath.getPath(source, target);
        validateShortestPathIsNull(path);

        List<Station> stations = path.getVertexList();
        int distance = (int) path.getWeight();

        int fee = AdditionalFeeStrategy.getFee(path);
        return getPathOf(stations, distance, fee, age);
    }

    public Path findShortestPath(Station source, Station target) {
        validateStations(source, target);
        return this.findShortestPath(source,target, ADULT_START_AGE);
    }

    private Path getPathOf(List<Station> stations, int distance, int fee, int age) {
        Path path = Path.of(stations, distance, fee);
        path.applyDiscountAgeStrategy(age, fee);
        return path;
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
