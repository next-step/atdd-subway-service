package nextstep.subway.path.domain;

import java.util.Objects;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> path;

    public PathFinder() {
        path = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public PathResponse findShortestPath(List<Sections> allSections, Station source, Station target) {
        drawPath(allSections);
        validateStation(source, target);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = findDijkstraShortestPath(source, target);
        validateShortestPath(shortestPath);
        return PathResponse.of(shortestPath.getVertexList(), shortestPath.getWeight());
    }

    private void validateShortestPath(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        if (Objects.isNull(shortestPath)) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }

    private GraphPath<Station, DefaultWeightedEdge> findDijkstraShortestPath(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(path);
        return dijkstraShortestPath.getPath(source, target);
    }

    private void validateStation(Station source, Station target) {
        if (isEquals(source, target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
        if (hasNot(source, target)) {
            throw new IllegalArgumentException("출발역 혹은 도착역이 존재하지 않습니다.");
        }
    }

    private boolean hasNot(Station source, Station target) {
        return !path.containsVertex(source) || !path.containsVertex(target);
    }

    private boolean isEquals(Station source, Station target) {
        return source.equals(target);
    }

    private void drawPath(List<Sections> allSections) {
        allSections.forEach(sections -> {
            addStations(sections.findOrderedAllStations());
            connectSections(sections.getSections());
        });
    }

    private void connectSections(List<Section> sections) {
        sections.forEach(this::addSectionToEdge);
    }

    private void addSectionToEdge(Section section) {
        path.setEdgeWeight(
                path.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance());
    }

    private void addStations(List<Station> allStations) {
        allStations.forEach(path::addVertex);
    }
}
