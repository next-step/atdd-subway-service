package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Distance;
import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DijkstraShortestPath implements Path {

    private final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final org.jgrapht.alg.shortestpath.DijkstraShortestPath<Long, Long> dijkstraShortestPath = new org.jgrapht.alg.shortestpath.DijkstraShortestPath(graph);

    @Override
    public Distance getWeight(final Station source, final Station target) {
        return new Distance((int) dijkstraShortestPath.getPath(source.getId(), target.getId()).getWeight());
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
    public Optional<List<Long>> getVertex(final Station source, final Station target) {
        final GraphPath<Long, Long> path = dijkstraShortestPath.getPath(source.getId(), target.getId());
        try {
            return Optional.of(path.getVertexList());
        }catch (NullPointerException e) {
            return Optional.empty();
        }
    }
}
