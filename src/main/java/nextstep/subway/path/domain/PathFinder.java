package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private PathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static PathFinder of(Sections sections) {
        validateNonEmpty(sections);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Section section : sections.getAll()) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());

            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }

        return new PathFinder(graph);
    }

    public Path findShortestPath(Station source, Station target) {
        validateRequestStation(source, target);

        return Path.of(findShortestPathByDijkstra(source, target));
    }

    private List<Station> findShortestPathByDijkstra(Station source, Station target) {
        try {
            return new DijkstraShortestPath<>(graph)
                    .getPath(source, target)
                    .getVertexList();
        } catch (NullPointerException nullPointerException) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }

    private static void validateNonEmpty(Sections sections) {
        if (sections.isEmpty()) {
            throw new IllegalArgumentException("빈 구간으로 조회할 수 없습니다.");
        }
    }

    private void validateRequestStation(Station source, Station target) {
        if (source == null) {
            throw new IllegalArgumentException("출발역은 null 일 수 없습니다.");
        }

        if (target == null) {
            throw new IllegalArgumentException("도착역은 null 일 수 없습니다.");
        }

        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
        }

        validateContainStation(source, target);
    }

    private void validateContainStation(Station source, Station target) {
        if (!graph.containsVertex(source)) {
            throw new IllegalArgumentException("출발역이 전체 구간에 포함되지 않았습니다.");
        }

        if (!graph.containsVertex(target)) {
            throw new IllegalArgumentException("도착역이 전체 구간에 포함되지 않았습니다.");
        }
    }
}
