package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Distance;
import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.path.exception.NotConnectedStation;
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

    public Route findShortestRoute(Long source, Long target) {
        existStationValidator(stations, source, target);
        sameDepartureAndArrivalStationValidator(source, target);

        final Station stationStart = getStation(stations, source);
        final Station stationEnd = getStation(stations, target);

        final List<Long> vertex = getVertex(source, target, stationStart, stationEnd);
        final Distance distance = path.getWeight(stationStart, stationEnd);

        return new Route(getShortestStations(stations, vertex), distance);
    }

    private List<Long> getVertex(final Long source, final Long target, final Station stationStart, final Station stationEnd) {
        return path.getVertex(stationStart, stationEnd)
                .orElseThrow(() -> new NotConnectedStation(String.format("departure : %d, arrival : %d", source, target)));
    }

    private void existStationValidator(final List<Station> stations, final Long source, final Long target) {
        getStation(stations, source);
        getStation(stations, target);
    }

    private Station getStation(final List<Station> stations, final Long stationId) {
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

    private List<Station> getShortestStations(final List<Station> stations, final List<Long> vertexList) {
        final List<Station> result = new ArrayList<>();
        for (Long stationId : vertexList) {
            Station station = getStation(stations, stationId);
            result.add(station);
        }
        return result;
    }
}
