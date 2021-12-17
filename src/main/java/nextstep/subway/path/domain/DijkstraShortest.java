package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DijkstraShortest implements PathStrategy {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath(graph);

    public Path getShortestPath(final List<Line> lines, final Station source, final Station target) {
        createEdge(lines);
        createVertex(lines);
        final List<Station> stations = getVertexes(source, target);
        return Path.of(getWeight(source, target), stations);
    }

    public Double getWeight(final Station source, final Station target) {
        final GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);
        if (Objects.isNull(graphPath)) {
            return Double.NaN;
        }
        return graphPath.getWeight();
    }

    public void createEdge(final List<Line> lines) {
        lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(graph::addVertex);
    }

    public void createVertex(final List<Line> lines) {
        lines.stream()
                .map(Line::getSectionList)
                .flatMap(Collection::stream)
                .forEach(section ->
                        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().getDistance()));
    }

    public List<Station> getVertexes(final Station source, final Station target) {
        final GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        if(Objects.isNull(path)){
            return Collections.emptyList();
        }
        return path.getVertexList();
    }
}
