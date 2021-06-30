package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PathFinder {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        setVertexAndEdgeWeight(getSectionsByLines(lines));
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public List<Station> findPaths(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
        }
        return Optional.of(dijkstraShortestPath.getPath(source, target).getVertexList()).orElseThrow(() -> new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다."));
    }

    public int getPathsDistance(Station source, Station target) {
        return (int) dijkstraShortestPath.getPathWeight(source, target);
    }

    private void setVertexAndEdgeWeight(List<Section> sections) {
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private List<Section> getSectionsByLines(List<Line> lines) {
        return lines.stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toList());
    }
}
