package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private static WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        lines.stream()
                .forEach(line -> setGraph(line.getSections()));
    }

    public static PathFinder of(List<Line> lines) {
        return new PathFinder(lines);
    }

    public void setGraph(List<Section> sections) {
        sections.stream()
                .forEach(section -> {
                    addVertex(section);
                    setEdgeWeight(section);
                });
    }

    public void addVertex(Section section) {
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
    }

    public void setEdgeWeight(Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }

    public GraphPath<Station, DefaultWeightedEdge> getShortestPath(Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(source, target);
    }

}
