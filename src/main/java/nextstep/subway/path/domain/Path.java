package nextstep.subway.path.domain;

import java.util.List;
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
        checkVertexExists(start, destination);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        checkPathExists(dijkstraShortestPath, start, destination);
        return PathResult.of(dijkstraShortestPath, start, destination);
    }

    private void checkVertexExists(Station start, Station destination) {
        if (!containsVertex(start) || !containsVertex(destination)) {
            throw new IllegalArgumentException("존재하는 노선안에 해당 역이 존재하지 않습니다.");
        }
    }

    private void checkPathExists(DijkstraShortestPath dijkstraShortestPath, Station start, Station destination) {
        if (dijkstraShortestPath.getPath(start, destination) == null) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }

    public boolean containsVertex(Station station) {
        return graph.containsVertex(station);
    }

    public boolean containsEdge(Station start, Station end) {
        return graph.containsEdge(start, end);
    }
}
