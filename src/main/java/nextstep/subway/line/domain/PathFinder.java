package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import nextstep.subway.line.exception.WrongPathException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(Line... lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
            DefaultWeightedEdge.class);
        addVertex(graph, lines);
        putEdgeWeight(graph, lines);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void addVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line[] lines) {
        Arrays.stream(lines)
            .map(Line::getStations)
            .flatMap(Collection::stream)
            .distinct()
            .forEach(graph::addVertex);
    }

    private void putEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
        Line[] lines) {
        Arrays.stream(lines)
            .map(Line::getSections)
            .forEach(sections -> sections.putEdgeWeight(graph));
    }

    public List<Station> shortestPath(Station from, Station to) {
        validateFromAndTo(from, to);
        return dijkstraShortestPath.getPath(from, to).getVertexList();
    }

    private void validateFromAndTo(Station from, Station to) {
        if (from.equals(to)) {
            throw new WrongPathException("출발역과 도착역이 동일합니다.");
        }
    }
}
