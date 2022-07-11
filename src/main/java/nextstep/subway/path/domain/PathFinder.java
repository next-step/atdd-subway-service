package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;

public class PathFinder {

    private static WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    public static final String ERROR_SAME_STATIONS = "출발역과 도착역은 달라야 합니다.";
    public static final String ERROR_NOT_EXISTED_STATION = "존재하지 않은 역은 조회 할 수 없습니다.";
    public static final String ERROR_NOT_CONNECTED_STATIONS = "출발역과 도착역이 연결되지 않았습니다.";


    public PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        lines.stream()
                .forEach(line -> setGraph(line.getSections()));
    }

    public static PathFinder of(List<Line> lines) {
        return new PathFinder(lines);
    }

    public void setGraph(List<Section> sections) {
        sections.stream()
                .forEach(section -> {
                    addVertex(section);
                    setEdgeWeight(section);
                });
    }

    public void addVertex(Section section) {
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
    }

    public void setEdgeWeight(Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }

    public GraphPath<Station, DefaultWeightedEdge> getShortestPath(Station source, Station target) {
        isStationsNotExist(source, target);
        isStationsSame(source, target);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
                .orElseThrow(() -> new IllegalArgumentException(ERROR_NOT_CONNECTED_STATIONS));
    }

    private void isStationsSame(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(ERROR_SAME_STATIONS);
        }
    }

    private void isStationsNotExist(Station source, Station target) {
        if (!graph.containsVertex(source) || !graph.containsVertex(target)) {
            throw new IllegalArgumentException(ERROR_NOT_EXISTED_STATION);
        }
    }

}
