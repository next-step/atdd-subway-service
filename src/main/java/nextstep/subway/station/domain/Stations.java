package nextstep.subway.station.domain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Stations {
    private final List<Station> stations;

    public Stations(final List<Station> stations) {
        this.stations = validStations(stations);
    }

    private List<Station> validStations(final List<Station> stations) {
        return Optional.ofNullable(stations)
            .orElseThrow(IllegalArgumentException::new);
    }

    public long countMatch(final List<Station> stationList) {
        if (this.stations.isEmpty()) {
            return -1;
        }

        final List<Station> validStations = validStations(stationList);

        return validStations.stream()
            .filter(stations::contains)
            .count();
    }

    public boolean contains(final Station station) {
        return stations.contains(station);
    }

    public List<StationPair> toStationPairs() {
        validateStationsSize();
        final List<StationPair> stationPairs = new LinkedList<>();
        addStationPair(stationPairs);

        return Collections.unmodifiableList(stationPairs);
    }

    private void validateStationsSize() {
        if (stations.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void addStationPair(final List<StationPair> stationPairs) {
        for (int i = 0; i < stations.size() - 1; i++) {
            stationPairs.add(new StationPair(stations.get(i), stations.get(i + 1)));
        }
    }

    public List<Station> toList() {
        return Collections.unmodifiableList(stations);
    }
}
