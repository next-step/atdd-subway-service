package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class Path {
    private GraphPath<Station, DefaultWeightedEdge> shortestPath;

    public Path(List<Line> lines, Station source, Station target) {
        validateIsSame(source, target);
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        for (Line line : lines) {
            addSection(graph, line.getSections().getSections());
        }

        validateContain(source, target, graph);
        getShortestPath(source, target, graph);
    }

    private void validateContain(Station source, Station target, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        if (!graph.containsVertex(source) || !graph.containsVertex(target)) {
            throw new IllegalArgumentException("존재하지 않은 출발역이나 도착역을 조회 할 수 없습니다.");
        }
    }

    private void validateIsSame(Station source, Station target) {
        if (source == target) {
            throw new IllegalArgumentException("출발역과 도착역이 같은 경우 최단 경로를 찾을 수 없습니다.");
        }
    }

    private void addSection(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private void getShortestPath(Station source, Station target, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        shortestPath =  dijkstraShortestPath.getPath(source, target);
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
}
