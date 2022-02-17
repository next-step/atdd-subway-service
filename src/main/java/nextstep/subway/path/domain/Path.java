package nextstep.subway.path.domain;

import java.util.List;
import java.util.Set;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private WeightedMultigraph<Station, SectionEdge> graph;

    public Path() {
        graph = new WeightedMultigraph(SectionEdge.class);
    }

    public static Path of(List<Line> lines) {
        Path path = new Path();
        lines.stream()
            .forEach(line -> path.addVertexAndEdge(line.getSections()));
        return path;
    }

    private void addVertexAndEdge(List<Section> sections) {
        sections.stream()
            .forEach(section -> {
                addVertex(section);
                addEdge(section);
            });
    }

    private void addVertex(Section section) {
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
    }

    private void addEdge(Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }

    public PathResult getShortestPath(Station start, Station destination) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return PathResult.of(dijkstraShortestPath, start, destination);
    }

    public boolean containsVertex(Station station){
        return graph.containsVertex(station);
    }

    public boolean containsEdge(Station start, Station end){
        return graph.containsEdge(start, end);
    }
}
