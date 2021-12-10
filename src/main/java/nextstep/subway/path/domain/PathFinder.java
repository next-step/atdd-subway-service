package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.exception.PathFindException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath;

    private PathFinder(DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static PathFinder of(Sections sections) {
        validateNonEmpty(sections);

        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);

        for (Section section : sections.getAll()) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());

            SectionEdge sectionEdge = SectionEdge.of(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
            graph.setEdgeWeight(sectionEdge, section.getDistance());
        }

        return new PathFinder(new DijkstraShortestPath<>(graph));
    }

    private static void validateNonEmpty(Sections sections) {
        if (sections.isEmpty()) {
            throw new PathFindException("빈 구간으로 조회할 수 없습니다.");
        }
    }

    public ShortestPath findShortestPath(Station source, Station target) {
        validateRequestStation(source, target);

        GraphPath<Station, SectionEdge> graphPath = getShortestPath(source, target);

        return ShortestPath.of(graphPath.getEdgeList(), graphPath.getVertexList(), (int) graphPath.getWeight());
    }

    private GraphPath<Station, SectionEdge> getShortestPath(Station source, Station target) {
        GraphPath<Station, SectionEdge> graphPath;
        try {
            graphPath = dijkstraShortestPath.getPath(source, target);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new PathFindException("출발역 또는 도착역이 전체 구간에 포함되지 않았습니다.");
        }

        if (graphPath == null) {
            throw new PathFindException("출발역과 도착역이 연결되어 있지 않습니다.");
        }

        return graphPath;
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
