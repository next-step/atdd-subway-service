package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private final WeightedMultigraph<Station, SectionWeightedEdge> graph;
    private final DijkstraShortestPath<Station, SectionWeightedEdge> dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph<>(SectionWeightedEdge.class);
        for (Line line : lines) {
            addStationAndSection(line);
        }
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void addStationAndSection(Line line) {
        for (Section section : line.getSections()) {
            SectionWeightedEdge edge = SectionWeightedEdge.of(section);
            graph.addVertex(edge.getUpStation());
            graph.addVertex(edge.getDownStation());
            graph.addEdge(edge.getUpStation(), edge.getDownStation(), edge);
            graph.setEdgeWeight(edge, edge.getDistance());
        }
    }

    public static PathFinder of(List<Line> lines) {
        return new PathFinder(lines);
    }

    public GraphPath<Station, SectionWeightedEdge> findShortestPath(Station source, Station target) {
        GraphPath<Station, SectionWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        return path;
    }
}
