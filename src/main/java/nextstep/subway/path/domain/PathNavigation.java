package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.EdgeFactory;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

public class PathNavigation {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> path;
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private static final String ERROR_MESSAGE_EQUALS_STATIONS = "동일한 역을 입력하였습니다.";
    private static final String ERROR_MESSAGE_NOT_EXISTED_STATIONS = "존재하지 않은 출발역이나 도착역이 있습니다.";
    private static final String ERROR_MESSAGE_NOT_CONNECTED_STATIONS = "역이 연결되어 있지 않습니다.";

    private PathNavigation(List<Line> lines) {
        validateNotNull(lines);
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines) {
            line.getStations().forEach(graph::addVertex);
            line.getSections().forEach(section ->
                    graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                            section.getDistance()));
        }

        path = new DijkstraShortestPath<>(graph);
    }

    public static PathNavigation by(List<Line> lines) {
        return new PathNavigation(lines);
    }

    public Path findShortestPath(Station source, Station target) {
        validateStations(source, target);

        GraphPath<Station, DefaultWeightedEdge> shortestPath = this.path.getPath(source, target);
        return Path.of(shortestPath.getVertexList(), (int) shortestPath.getWeight());
    }

    private void validateStations(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_EQUALS_STATIONS);
        }

        if (!graph.containsVertex(source) || !graph.containsVertex(target)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_EXISTED_STATIONS);
        }

        if (!graph.containsEdge(source, target)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_CONNECTED_STATIONS);
        }
    }

    private void validateNotNull(List<Line> lines) {
        if (Objects.isNull(lines)) {
            throw new IllegalArgumentException("lines is Null");
        }
    }
}
