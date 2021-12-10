package nextstep.subway.path.domain;

import nextstep.subway.common.exception.ServiceException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResult;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DefaultPathFinder implements PathFinder {
    public PathResult findShortCut(Set<Line> lines, Station source, Station target) {
        validateParameters(source, target);
        WeightedMultigraph<Station, SubwayWeightedEdge> graph = new WeightedMultigraph<>(SubwayWeightedEdge.class);
        addStationToGraph(lines, graph);
        linkAllSections(lines, graph);
        DijkstraShortestPath<Station, SubwayWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
                .map(PathResult::of)
                .orElseThrow(() -> new ServiceException("최단 경로를 찾을 수 없습니다"));
    }

    private void validateParameters(Station source, Station target) {
        if (source.equals(target)) {
            throw new ServiceException("출발지와 목적지가 같습니다.");
        }
    }

    private void linkAllSections(Set<Line> lines, WeightedMultigraph<Station, SubwayWeightedEdge> graph) {
        Set<Section> sections = getAllSections(lines);
        for (Section section : sections) {
            SubwayWeightedEdge subwayWeightedEdge = graph.addEdge(section.getUpStation(), section.getDownStation());
            subwayWeightedEdge.setSection(section);
            graph.setEdgeWeight(subwayWeightedEdge, section.getDistance());
        }
    }

    private void addStationToGraph(Set<Line> lines, WeightedMultigraph<Station, SubwayWeightedEdge> graph) {
        Set<Station> stations = getAllStations(lines);
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private Set<Station> getAllStations(Set<Line> lines) {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toSet());
    }

    private Set<Section> getAllSections(Set<Line> lines) {
        return lines.stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toSet());
    }
}
