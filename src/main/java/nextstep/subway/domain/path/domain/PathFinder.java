package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.path.exception.NotConnectedStation;
import nextstep.subway.domain.path.exception.SameDepartureAndArrivalStationException;
import nextstep.subway.domain.path.exception.StationNotFoundException;
import nextstep.subway.domain.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class PathFinder {

    private final Path path = new DijkstraShortestPath();

    protected PathFinder() {
    }

    public PathFinder(final List<Line> lines) {
        createGraph(lines);
    }

    private void createGraph(final List<Line> lines) {
        path.createVertex(lines);
        path.createEdge(lines);
    }

    public List<Station> findShortestRoute(List<Station> stations, Long source, Long target) {
        existStationValidator(stations, source, target);
        sameDepartureAndArrivalStationValidator(source, target);
        final List<Long> vertexList = getVertex(source, target);
        return getShortestStations(stations, vertexList);
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

    private List<Long> getVertex(final Long source, final Long target) {
        return path.getVertex(source, target)
                .orElseThrow(() -> new NotConnectedStation(String.format("departure : %d, arrival : %d", source, target)));
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

    public int findShortestDistance(Long source, Long target) {
        return path.getWeight(source, target);
    }
}
