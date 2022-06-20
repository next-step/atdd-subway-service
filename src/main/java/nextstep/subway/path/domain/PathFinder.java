package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFindStrategy;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> path = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private PathFindStrategy strategy;

    public void determinePathFindStrategy(PathFindStrategy strategy) {
        this.strategy = strategy;
    }

    public PathResponse searchShortestPath(List<Section> sections, Station source, Station target) {
        validateSameSourceAndTarget(source, target);
        registerSections(sections);

        Path shortestPath = strategy.searchShortestPath(this.path, source, target);
        return PathResponse.of(shortestPath);
    }

    private void registerSections(List<Section> sections) {
        addVertexes(sections);
        addWeightedEdges(sections);
    }
    private void addVertexes(List<Section> sections) {
        List<Station> stations = sections.stream()
            .map(Section::getStationPair)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());
        stations.forEach(station -> this.path.addVertex(station));
    }

    private void addWeightedEdges(List<Section> sections) {
        sections.forEach(section -> this.path.setEdgeWeight(
            this.path.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().value())
        );
    }

    private void validateSameSourceAndTarget(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 동일할 수 없습니다.");
        }
    }



}
