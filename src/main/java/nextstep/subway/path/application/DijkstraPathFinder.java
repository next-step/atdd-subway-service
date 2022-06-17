package nextstep.subway.path.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class DijkstraPathFinder implements PathFinder {
    private static final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
            DefaultWeightedEdge.class);
    private final List<Section> sections = new ArrayList<>();

    public DijkstraPathFinder() {
    }

    public void initGraph(Set<Line> lines) {
        lines.forEach(this::initGraph);
    }

    private void initGraph(Line line) {
        initGraph(line.sections());
    }

    private void initGraph(Sections sections) {
        initGraph(sections.sections());
    }

    private void initGraph(List<Section> sections) {
        for (Section section : sections) {
            this.sections.add(section);
            graph.addVertex(section.upStation());
            graph.addVertex(section.downStation());
            graph.setEdgeWeight(graph.addEdge(section.upStation(), section.downStation()), section.distanceValue());
        }
    }

    public Path shortestPath(Station source, Station target) {
        List<Station> stations = shortestPathVertexList(source, target);
        int distance = shortestPathWeight(source, target);
        return Path.valueOf(stations, Distance.valueOf(distance), findLinesOfPath(stations));
    }

    private List<Station> shortestPathVertexList(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = getShortestPath(source, target);
        return shortestPath.getVertexList();
    }

    private int shortestPathWeight(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = getShortestPath(source, target);
        return (int) shortestPath.getWeight();
    }

    private GraphPath<Station, DefaultWeightedEdge> getShortestPath(Station source, Station target) {
        validateNotSameStation(source, target);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(source, target);
        validatePathNotNull(shortestPath);
        return shortestPath;
    }

    private void validateNotSameStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }
    }

    private void validatePathNotNull(GraphPath<Station, DefaultWeightedEdge> path) {
        if (Objects.isNull(path)) {
            throw new NotFoundException("최단경로를 조회할 수 없습니다.");
        }
    }

    private Set<Line> findLinesOfPath(List<Station> stations) {
        return sections.stream()
                .filter(section -> containSectionFromStations(section, stations))
                .map(Section::line)
                .collect(Collectors.toSet());
    }

    private boolean containSectionFromStations(Section section, List<Station> stations) {
        Map<Station, Station> sections = new HashMap<>();
        for (int i = 0; i < stations.size() - 1; i++) {
            sections.put(stations.get(i), stations.get(i + 1));
        }
        return isEqualUpStationAndDownStation(sections, section);
    }

    private boolean isEqualUpStationAndDownStation(Map<Station, Station> sections, Section section) {
        return isEqualUpStationAndDownStationForward(sections, section) || isEqualUpStationAndDownStationReverse(
                sections, section);
    }

    private boolean isEqualUpStationAndDownStationForward(Map<Station, Station> sections, Section section) {
        if (sections.containsKey(section.upStation())) {
            return isEqualDownStation(sections, section);
        }
        return false;
    }

    private boolean isEqualUpStationAndDownStationReverse(Map<Station, Station> sections, Section section) {
        if (sections.containsKey(section.downStation())) {
            return isEqualUpStation(sections, section);
        }
        return false;
    }

    private boolean isEqualDownStation(Map<Station, Station> sections, Section section) {
        return sections.get(section.upStation()).equals(section.downStation());
    }

    private boolean isEqualUpStation(Map<Station, Station> sections, Section section) {
        return sections.get(section.downStation()).equals(section.upStation());
    }
}
