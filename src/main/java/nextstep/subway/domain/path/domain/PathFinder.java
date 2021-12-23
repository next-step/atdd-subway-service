package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Distance;
import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.line.domain.Section;
import nextstep.subway.domain.path.exception.SameDepartureAndArrivalStationException;
import nextstep.subway.domain.path.exception.StationNotFoundException;
import nextstep.subway.domain.station.domain.Station;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {

    private final Path path = new DijkstraShortestPath();
    private List<Station> stations;

    protected PathFinder() {
    }

    public PathFinder(final List<Line> lines) {
        createGraph(lines);
        this.stations = lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    private void createGraph(final List<Line> lines) {
        path.createVertex(lines);
        path.createEdge(lines);
    }

    public Route findShortestRoute(Long source, Long target, List<Line> lines) {
        existStationValidator(stations, source, target);
        sameDepartureAndArrivalStationValidator(source, target);

        final Station stationStart = getStation(stations, source);
        final Station stationEnd = getStation(stations, target);

        final List<Station> vertex = path.getVertex(stations, stationStart, stationEnd);
        final Distance distance = path.getWeight(stationStart, stationEnd);
        List<Section> shortestSection = findShortestSection(lines, vertex);

        return new Route(vertex, shortestSection, distance);
    }

    private List<Section> findShortestSection(List<Line> lines, List<Station> stations) {
        List<Section> sections = new ArrayList<>();

        for (int i=0; i<stations.size() - 1; i++) {
            Station upStation = stations.get(i);
            Station downStation = stations.get(i+1);

            sections.add(findSection(lines, upStation, downStation));
        }
        return sections;
    }

    private Section findSection(List<Line> lines, Station upStation, Station downStation) {
        return lines.stream()
                .map(Line::getSections)
                .flatMap(Collection::stream)
                .filter(section -> section.getUpStation().equals(upStation) && section.getDownStation().equals(downStation))
                .findFirst()
                .get();
    }

    private void existStationValidator(final List<Station> stations, final Long source, final Long target) {
        getStation(stations, source);
        getStation(stations, target);
    }

    public static Station getStation(final List<Station> stations, final Long stationId) {
        return stations.stream()
                .filter(station -> station.getId().equals(stationId))
                .findFirst()
                .orElseThrow(() -> new StationNotFoundException(String.format("stationId : %d", stationId)));
    }

    private void sameDepartureAndArrivalStationValidator(final Long source, final Long target) {
        if (source.equals(target)) {
            throw new SameDepartureAndArrivalStationException(String.format("departure : %d, arrival : %d", source, target));
        }
    }
}
