package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private WeightedMultigraph<Station, SectionEdge> graph;
    private DijkstraShortestPath shortestPath;

    public Path() {
        graph = new WeightedMultigraph(SectionEdge.class);
        shortestPath = new DijkstraShortestPath(graph);
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
        SectionEdge sectionEdge = graph.addEdge(section.getUpStation(), section.getDownStation());
        sectionEdge.setSection(section);
        graph.setEdgeWeight(sectionEdge, section.getDistance());
    }

    public PathResult getShortestPath(Station start, Station destination) {
        checkVertexValidity(start, destination);
        checkPathExists(start, destination);
        return PathResult.of(getVertexes(start, destination),
            getDistance(start, destination),
            fare(start, destination));
    }

    private void checkVertexValidity(Station start, Station destination) {
        if (start.equals(destination)) {
            throw new IllegalArgumentException("출발역과 도착역이 같아 경로를 찾을 수 없습니다.");
        }
        if (!containsVertex(start) || !containsVertex(destination)) {
            throw new IllegalArgumentException("존재하는 노선안에 해당 역이 존재하지 않습니다.");
        }
    }

    private void checkPathExists(Station start, Station destination) {
        if (shortestPath.getPath(start, destination) == null) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }

    public boolean containsVertex(Station station) {
        return graph.containsVertex(station);
    }

    public boolean containsEdge(Station start, Station end) {
        return graph.containsEdge(start, end);
    }

    private List<Station> getVertexes(Station start, Station destination) {
        return shortestPath.getPath(start, destination).getVertexList();
    }

    private int getDistance(Station start, Station destination) {
        return (int) shortestPath.getPathWeight(start, destination);
    }

    private Fare fare(Station start, Station destination) {
        return Fare.of(getDistance(start, destination),
            getAdditionalFare(start, destination));
    }

    private int getAdditionalFare(Station start, Station destination) {
        GraphPath<Station, SectionEdge> graphPath = shortestPath.getPath(start, destination);
        return graphPath.getEdgeList().stream()
            .map(SectionEdge::getAdditionalFare)
            .max(Integer::compareTo)
            .orElse(0);
    }

}
