package nextstep.subway.path.domain;

import static nextstep.subway.exception.ExceptionMessage.*;

import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private final Set<Section> sections;

    public PathFinder(Set<Section> sections) {
        this.sections = sections;
    }

    public GraphPath<Station, Section> getShortestPaths(Station sourceStation, Station targetStation) {
        validate(sourceStation, targetStation);
        return getPath(sourceStation, targetStation);
    }

    private GraphPath<Station, Section> getPath(Station sourceStation, Station targetStation) {
        Set<Station> stations = getAllStation();
        WeightedMultigraph<Station, DefaultWeightedEdge> graph =
            new WeightedMultigraph(DefaultWeightedEdge.class);
        for (Station station : stations) {
            graph.addVertex(station);
        }
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(),
                section.getDownStation()), section.getDistance());
        }
        return new DijkstraShortestPath(graph).getPath(sourceStation, targetStation);
    }

    private void validate(Station sourceStation, Station targetStation) {
        validateSameStation(sourceStation, targetStation);
        validateExistStation(sourceStation, targetStation);
    }

    private void validateSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new BadRequestException(SAME_STATION);
        }
    }

    private void validateExistStation(Station sourceStation, Station targetStation) {
        Set<Station> allStation = getAllStation();
        if (!allStation.contains(sourceStation) || !allStation.contains(targetStation)) {
            throw new BadRequestException(NOT_EXIST_STATION);
        }
    }

    private Set<Station> getAllStation() {
        Set<Station> upStations = getUpStations();
        Set<Station> downStations = getDownStations();
        upStations.addAll(downStations);
        return upStations;
    }

    private Set<Station> getUpStations() {
        return sections.stream()
            .map(Section::getUpStation)
            .collect(Collectors.toSet());
    }

    private Set<Station> getDownStations() {
        return sections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toSet());
    }
}
