package nextstep.subway.line.domain;

import static nextstep.subway.common.Message.MESSAGE_EQUALS_START_STATION_END_STATION;
import static nextstep.subway.common.Message.MESSAGE_NOT_CONNECTED_START_STATION_AND_END_STATION;
import static nextstep.subway.common.Message.MESSAGE_NOT_EXISTS_START_STATION_OR_END_STATION;

import java.util.Collections;
import java.util.List;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.line.exception.LineNotFoundException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Navigation {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap;
    private final List<Line> lines;

    public static Navigation of(List<Line> lines) {
        Navigation navigation = new Navigation(lines);
        navigation.subwayMapInit();
        return navigation;
    }

    private Navigation(List<Line> lines) {
        if (lines.isEmpty()) {
            throw new LineNotFoundException();
        }
        this.subwayMap = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.lines = lines;
    }

    private void subwayMapInit() {
        for (Line line : lines) {
            addStations(line);
            addSections(line);
        }
    }

    private void addStations(final Line line) {
        for (Station station : line.getStations()) {
            subwayMap.addVertex(station);
        }
    }

    private void addSections(final Line line) {
        List<Section> sections = line.getSections();
        for (Section section : sections) {
            subwayMap.setEdgeWeight(
                subwayMap.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance());
        }
    }

    public PathResponse findFastPath(final Station source, final Station target) {
        validateEqualsSourceAndTarget(source, target);
        validateNotExistsStartStationOrEndStation(source, target);
        return getStations(source, target);
    }

    void validateEqualsSourceAndTarget(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(MESSAGE_EQUALS_START_STATION_END_STATION.getMessage());
        }
    }

    void validateNotExistsStartStationOrEndStation(Station source, Station target) {
        if ((!subwayMap.containsVertex(source) || !subwayMap.containsVertex(target))) {
            throw new IllegalArgumentException(MESSAGE_NOT_EXISTS_START_STATION_OR_END_STATION.getMessage());
        }
    }

    private PathResponse getStations(Station source, Station target) {
        List<GraphPath<Station, DefaultWeightedEdge>> paths = new KShortestPaths<>(subwayMap, 100)
            .getPaths(source, target);
        validateNotConnectedStation(paths);
        return findShotPath(paths);
    }

    void validateNotConnectedStation(List<GraphPath<Station, DefaultWeightedEdge>> paths) {
        if (paths.isEmpty()) {
            throw new IllegalArgumentException(
                MESSAGE_NOT_CONNECTED_START_STATION_AND_END_STATION.getMessage());
        }
    }

    private PathResponse findShotPath(List<GraphPath<Station, DefaultWeightedEdge>> paths) {
        GraphPath<Station, DefaultWeightedEdge> shortPath = paths.get(0);
        List<Station> shortStation = shortPath.getVertexList();
        return PathResponse.ofList(Collections.unmodifiableList(shortStation), shortPath.getWeight());
    }

}
