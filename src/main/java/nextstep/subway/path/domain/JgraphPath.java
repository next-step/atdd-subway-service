package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class JgraphPath {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public GraphPath getShortestPath(final List<Line> lines, final Station start, final Station destination) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        addStation(lines);
        addEdgeWeight(lines);
        return new DijkstraShortestPath(graph).getPath(start, destination);
    }

    private void addStation(final List<Line> lines) {
        for (Line line : lines) {
            line.getSortedStations()
                    .stream()
                    .forEach(it -> graph.addVertex(it));
        }
    }

    private void addEdgeWeight(final List<Line> lines) {
        for (Line line : lines) {
            line.getSections()
                    .stream()
                    .forEach(it -> addSection(it));
        }
    }

    private void addSection(final Section section) {
        final DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
        graph.setEdgeWeight(edge, section.getDistance());
    }
}
