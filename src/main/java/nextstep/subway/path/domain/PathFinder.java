package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private static final String SOURCE_TARGET_EQUAL_ERROR_MESSAGE = "출발역과 도착역이 같은 경우 최단 경로를 조회할 수 없습니다.";
    private static final String SOURCE_TARGET_NOT_CONNECTED_ERROR_MESSAGE = "출발역과 도착역이 연결이 되어 있지 않은 경우 최단 경로를 조회할 수 없습니다.";
    private static final String SOURCE_NOT_FOUND_ERROR_MESSAGE = "출발역이 존재하지 않을 경우 최단 경로를 조회할 수 없습니다.";
    private static final String TARGET_NOT_FOUND_ERROR_MESSAGE = "도착역이 존재하지 않을 경우 최단 경로를 조회할 수 없습니다.";

    private final WeightedGraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(final List<Line> lines) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertices(lines);
        addEdges(lines);
    }

    private void addVertices(final List<Line> lines) {
        final List<Station> stations = lines.stream()
            .map(Line::getStationsInOrder)
            .flatMap(List::stream)
            .distinct()
            .collect(Collectors.toList());
        stations.forEach(graph::addVertex);
    }

    private void addEdges(final List<Line> lines) {
        final List<Section> sections = lines.stream()
            .map(Line::getSections)
            .map(Sections::getSections)
            .flatMap(List::stream)
            .collect(Collectors.toList());
        sections.forEach(this::setEdgeWeight);
    }

    private void setEdgeWeight(final Section section) {
        graph.setEdgeWeight(
            graph.addEdge(section.getUpStation(), section.getDownStation()),
            section.getDistance()
        );
    }

    public Path findPath(final Station source, final Station target) {
        validateFindPath(source, target);
        final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraPath = new DijkstraShortestPath<>(
            graph);
        final GraphPath<Station, DefaultWeightedEdge> path = dijkstraPath.getPath(source, target);
        validatePath(path);
        return new Path(path.getVertexList(), (int) path.getWeight());
    }

    private void validateFindPath(final Station source, final Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(SOURCE_TARGET_EQUAL_ERROR_MESSAGE);
        }
        if (!graph.containsVertex(source)) {
            throw new IllegalArgumentException(SOURCE_NOT_FOUND_ERROR_MESSAGE);
        }
        if (!graph.containsVertex(target)) {
            throw new IllegalArgumentException(TARGET_NOT_FOUND_ERROR_MESSAGE);
        }
    }

    private void validatePath(final GraphPath<Station, DefaultWeightedEdge> path) {
        if (Objects.isNull(path)) {
            throw new IllegalArgumentException(SOURCE_TARGET_NOT_CONNECTED_ERROR_MESSAGE);
        }
    }
}
