package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    public static final String ERROR_PATH_NOT_FOUND = "경로를 찾을 수 없습니다.";
    public static final String ERROR_DUPLICATE_STATION = "출발역과 도착역이 같습니다.";
    private static final String ERROR_START_STATION_NOT_FOUND = "출발역이 찾을 수 없습니다.";
    public static final String ERROR_END_STATION_NOT_FOUND = "도착역을 찾을 수 없습니다.";

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        initGraph(lines);
    }

    public Path findShortestPath(Station source, Station target) {
        checkStations(source, target);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath graphPath = dijkstraShortestPath.getPath(source, target);
        if (isNotFoundPath(graphPath)) {
            throw new IllegalArgumentException(ERROR_PATH_NOT_FOUND);
        }

        return new Path(graphPath.getVertexList(), (int)graphPath.getWeight());
    }

    private void checkStations(Station source, Station target) {
        if (isEquals(source, target)) {
            throw new IllegalArgumentException(ERROR_DUPLICATE_STATION);
        }

        if (isNotFoundStation(source)) {
            throw new IllegalArgumentException(ERROR_START_STATION_NOT_FOUND);
        }

        if (isNotFoundStation(target)) {
            throw new IllegalArgumentException(ERROR_END_STATION_NOT_FOUND);
        }
    }

    private boolean isNotFoundStation(Station station) {
        return !graph.containsVertex(station);
    }

    private boolean isEquals(Station source, Station target) {
        return source.equals(target);
    }

    private boolean isNotFoundPath(GraphPath graphPath) {
        return Objects.isNull(graphPath);
    }

    private void initGraph(List<Line> lines) {
        lines.stream()
            .map(Line::getSections)
            .forEach(sections -> {
                addAllVertex(sections.getStations());
                setEdgeWeight(sections.getSections());
            });
    }

    private void setEdgeWeight(List<Section> sections) {
        sections.forEach(section -> graph.setEdgeWeight(
            graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().getDistance())
        );
    }

    private void addAllVertex(Set<Station> stations) {
        stations.forEach(station -> graph.addVertex(station));
    }
}
