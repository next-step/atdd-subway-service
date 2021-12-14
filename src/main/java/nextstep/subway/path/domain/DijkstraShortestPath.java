package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class DijkstraShortestPath implements Path {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final org.jgrapht.alg.shortestpath.DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new org.jgrapht.alg.shortestpath.DijkstraShortestPath(graph);

    @Override
    public double getWeight(final Station source, final Station target) {
        return dijkstraShortestPath.getPath(source, target).getWeight();
    }

    @Override
    public void createEdge(final List<Line> lines) {
        lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(graph::addVertex);
    }

    @Override
    public void createVertex(final List<Line> lines) {
        lines.stream()
                .map(Line::getSectionList)
                .flatMap(Collection::stream)
                .forEach(section ->
                        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().getDistance()));
    }

    @Override
    public List<Station> getVertexes(final Station source, final Station target) {
        final GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        if(Objects.isNull(path)){
            return null;
        }
        return path.getVertexList();
    }
}
