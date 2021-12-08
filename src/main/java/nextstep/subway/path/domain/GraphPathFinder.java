package nextstep.subway.path.domain;

import static nextstep.subway.exception.ExceptionMessage.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.station.domain.Station;

@Component
public class GraphPathFinder implements PathFinder {

    @Override
    public PathResponse getShortestPaths(Set<Section> sections, Station sourceStation, Station targetStation) {
        validate(sections, sourceStation, targetStation);
        GraphPath<Station, Section> findPath = getPath(sections, sourceStation, targetStation);
        return convertPathResponse(findPath.getVertexList(), findPath.getWeight());
    }

    private GraphPath<Station, Section> getPath(Set<Section> sections, Station sourceStation, Station targetStation) {
        Set<Station> stations = getAllStation(sections);
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

    private void validate(Set<Section> sections, Station sourceStation, Station targetStation) {
        validateSameStation(sourceStation, targetStation);
        validateExistStation(sections, sourceStation, targetStation);
    }

    private void validateSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new BadRequestException(SAME_STATION);
        }
    }

    private void validateExistStation(Set<Section> sections, Station sourceStation, Station targetStation) {
        Set<Station> allStation = getAllStation(sections);
        if (!allStation.contains(sourceStation) || !allStation.contains(targetStation)) {
            throw new BadRequestException(NOT_EXIST_STATION);
        }
    }

    private Set<Station> getAllStation(Set<Section> sections) {
        Set<Station> upStations = getUpStations(sections);
        Set<Station> downStations = getDownStations(sections);
        upStations.addAll(downStations);
        return upStations;
    }

    private Set<Station> getUpStations(Set<Section> sections) {
        return sections.stream()
            .map(Section::getUpStation)
            .collect(Collectors.toSet());
    }

    private Set<Station> getDownStations(Set<Section> sections) {
        return sections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toSet());
    }

    private PathResponse convertPathResponse(List<Station> stations, double weight) {
        return new PathResponse(convertPathStationResponses(stations), (int)weight);
    }

    private List<PathStationResponse> convertPathStationResponses(List<Station> stations) {
        return stations.stream()
            .map(station -> PathStationResponse.of(station.getId(), station.getName(), station.getCreatedDate()))
            .collect(Collectors.toList());
    }
}
