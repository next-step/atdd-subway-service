package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;
import nextstep.subway.line.domain.Line;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private WeightedMultigraph<Long, DefaultWeightedEdge> graph;

    public PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(lines, graph);
        setEdgeWeight(lines, graph);
    }

    private void addVertex(List<Line> lines, WeightedMultigraph<Long, DefaultWeightedEdge> graph){
        lines.stream()
                .flatMap(line -> line.getStations().stream())
                .forEach(station -> graph.addVertex(station.getId()));
    }

    private void setEdgeWeight(List<Line> lines, WeightedMultigraph<Long, DefaultWeightedEdge> graph){
        lines.stream()
                .flatMap(line -> line.getSections().getSections().stream())
                .forEach(section -> graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation().getId(), section.getDownStation().getId()),
                        section.getDistance()));
    }

    public Path findPath(Long source, Long target) {
        validateSameSourceTarget(source, target);
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        try {
            GraphPath<Long, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);
            return new Path(graphPath.getVertexList(), (int) graphPath.getWeight());
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
