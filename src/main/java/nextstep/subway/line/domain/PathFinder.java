package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import nextstep.subway.line.exception.WrongPathException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(Line... lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
            DefaultWeightedEdge.class);
        addVertex(graph, lines);
        putEdgeWeight(graph, lines);
        this.graph = graph;
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
        validateArgument(from, to);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
            graph);

        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(from, to);
        validatePath(path);

        return path.getVertexList();
    }

    private void validateArgument(Station from, Station to) {
        if (from.equals(to)) {
            throw new WrongPathException("출발역과 도착역이 동일합니다.");
        }

        if (!graph.containsVertex(from) || !graph.containsVertex(to)) {
            throw new WrongPathException("존재하지 않는 역입니다.");
        }
    }

    private void validatePath(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new WrongPathException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }
}
