package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class Path {
    private GraphPath<Station, SectionEdge> shortestPath;

    public Path(List<Line> lines, Station source, Station target) {
        validateIsSameStation(source, target);
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<Station, SectionEdge>(SectionEdge.class);
        for (Line line : lines) {
            addSection(graph, line.getSections());
        }

        validateGraphContainStations(source, target, graph);
        getShortestPath(source, target, graph);
    }

    private void validateIsSameStation(Station source, Station target) {
        if (source == target) {
            throw new IllegalArgumentException("출발역과 도착역이 같은 경우 최단 경로를 찾을 수 없습니다.");
        }
    }

    private void addSection(WeightedMultigraph<Station, SectionEdge> graph, List<Section> sections) {
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            SectionEdge sectionEdge = SectionEdge.of(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
            graph.setEdgeWeight(sectionEdge, section.getDistance());
        }
    }

    private void validateGraphContainStations(Station source, Station target, WeightedMultigraph<Station, SectionEdge> graph) {
        if (!graph.containsVertex(source) || !graph.containsVertex(target)) {
            throw new IllegalArgumentException("존재하지 않은 출발역이나 도착역을 조회 할 수 없습니다.");
        }
    }

    private void getShortestPath(Station source, Station target, WeightedMultigraph<Station, SectionEdge> graph) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        shortestPath = dijkstraShortestPath.getPath(source, target);
        if (shortestPath == null) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되어있지 않습니다.");
        }
    }

    public List<Station> findShortestPath() {
        return shortestPath.getVertexList();
    }

    public int findWeight() {
        return (int) shortestPath.getWeight();
    }

    public List<Line> findLines() {
        return shortestPath.getEdgeList().stream()
                .map(SectionEdge::getLine)
                .collect(Collectors.toList());
    }

    public List<StationResponse> getStationResponses() {
        return findShortestPath().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
