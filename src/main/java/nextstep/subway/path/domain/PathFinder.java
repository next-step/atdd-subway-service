package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final WeightedGraph<Station, DefaultEdge> graph;

    public PathFinder(final List<Line> lines) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        addVertices(lines);
        addEdges(lines);
    }

    private void addVertices(final List<Line> lines) {
        final List<Station> stations = lines.stream()
            .map(Line::getStationsInOrder)
            .flatMap(List::stream)
            .distinct()
            .collect(Collectors.toList());
        stations.forEach(graph::addVertex);
    }

    private void addEdges(final List<Line> lines) {
        final List<Section> sections = lines.stream()
            .map(Line::getSections)
            .map(Sections::getSections)
            .flatMap(List::stream)
            .collect(Collectors.toList());
        sections.forEach(this::setEdgeWeight);
    }

    private void setEdgeWeight(final Section section) {
        graph.setEdgeWeight(
            graph.addEdge(section.getUpStation(), section.getDownStation()),
            section.getDistance()
        );
    }

    public Path findPath(final Station source, final Station target) {
        final DijkstraShortestPath dijkstraPath = new DijkstraShortestPath(graph);
        final GraphPath<Station, DefaultWeightedEdge> path = dijkstraPath.getPath(source, target);
        return new Path(path.getVertexList(), (int) path.getWeight());
    }
}
