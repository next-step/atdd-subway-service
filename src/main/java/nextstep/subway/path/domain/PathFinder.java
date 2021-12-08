package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        addVertexes(lines, graph);
        setEdgeWeights(lines, graph);
    }

    public static PathFinder ofList(List<Line> lines) {
        return new PathFinder(lines);
    }

    public Path findShortestPath(Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath(source, target);
        return Path.of(path.getVertexList(), path.getWeight());
    }

    private void setEdgeWeights(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        lines.stream()
                .map(line -> line.getSections())
                .map(sections -> sections.getSections())
                .flatMap(List::stream)
                .collect(Collectors.toList())
                .forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().getValue()));
    }

    private void addVertexes(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        lines.stream()
                .map(line -> line.getSections())
                .map(sections -> sections.getStations())
                .flatMap(List::stream)
                .forEach(graph::addVertex);
    }
}
