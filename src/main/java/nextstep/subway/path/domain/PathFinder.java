package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.exception.PathFindException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    private PathFinder(DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static PathFinder of(Sections sections) {
        validateNonEmpty(sections);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Section section : sections.getAll()) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());

            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }

        return new PathFinder(new DijkstraShortestPath<>(graph));
    }

    private static void validateNonEmpty(Sections sections) {
        if (sections.isEmpty()) {
            throw new PathFindException("빈 구간으로 조회할 수 없습니다.");
        }
    }

    public Path findShortestPath(Station source, Station target) {
        validateRequestStation(source, target);

        try {
            GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);

            return Path.of(graphPath.getVertexList(), (int) graphPath.getWeight());
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new PathFindException("출발역 또는 도착역이 전체 구간에 포함되지 않았습니다.");
        } catch (NullPointerException nullPointerException) {
            throw new PathFindException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }

    private void validateRequestStation(Station source, Station target) {
        if (source == null) {
            throw new PathFindException("출발역은 null 일 수 없습니다.");
        }

        if (target == null) {
            throw new PathFindException("도착역은 null 일 수 없습니다.");
        }

        if (source.equals(target)) {
            throw new PathFindException("출발역과 도착역은 같을 수 없습니다.");
        }
    }
}
