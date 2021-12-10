package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Line;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Collection;
import java.util.List;

public class DijkstraShortestPath implements Path {

    private final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final org.jgrapht.alg.shortestpath.DijkstraShortestPath<Long, Long> dijkstraShortestPath = new org.jgrapht.alg.shortestpath.DijkstraShortestPath(graph);

    @Override
    public int getWeight(final Long source, final Long target) {
        return (int) dijkstraShortestPath.getPath(source, target).getWeight();
    }

    @Override
    public void createVertex(final List<Line> lines) {
        lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(station -> graph.addVertex(station.getId()));
    }

    @Override
    public void createEdge(final List<Line> lines) {
        lines.stream()
                .map(Line::getSections)
                .flatMap(Collection::stream)
                .forEach(section ->
                        graph.setEdgeWeight(graph.addEdge(section.getUpStation().getId(), section.getDownStation().getId()), section.getDistance().getDistance()));
    }

    @Override
    public List<Long> getVertex(final Long source, final Long target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }
}
