package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;
import nextstep.subway.line.domain.Line;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private final WeightedMultigraph<Long, SectionEdge> graph;

    public PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph<>(SectionEdge.class);
        addVertex(lines, graph);
        setEdgeWeight(lines, graph);
    }

    private void addVertex(List<Line> lines, WeightedMultigraph<Long, SectionEdge> graph){
        lines.stream()
                .flatMap(line -> line.getStations().stream())
                .forEach(station -> graph.addVertex(station.getId()));
    }

    private void setEdgeWeight(List<Line> lines, WeightedMultigraph<Long, SectionEdge> graph) {
        lines.stream()
                .map(Line::getSections)
                .flatMap(sections -> sections.getSections().stream())
                .forEach(section -> {
                    SectionEdge sectionEdge = graph.addEdge(
                            section.getUpStation().getId(), section.getDownStation().getId());
                    sectionEdge.setSection(section);
                    graph.setEdgeWeight(sectionEdge, section.getDistance());
                });
    }

    public Path findPath(Long source, Long target) {
        validateSameSourceTarget(source, target);
        DijkstraShortestPath<Long, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        try {
            GraphPath<Long, SectionEdge> graphPath = dijkstraShortestPath.getPath(source, target);
            return new Path(graphPath.getVertexList(), (int) graphPath.getWeight(), graphPath.getEdgeList());
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("경로를 찾을 수 없습니다");
        }
    }

    private void validateSameSourceTarget(Long sourceId, Long targetId) {
        if (Objects.equals(sourceId, targetId)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다");
        }
    }
}
