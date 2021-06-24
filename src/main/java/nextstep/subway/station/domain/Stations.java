package nextstep.subway.station.domain;

import java.util.List;
import java.util.Optional;

public class Stations {
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = validStations(stations);
    }

    private List<Station> validStations(List<Station> stations) {
        return Optional.ofNullable(stations)
            .orElseThrow(IllegalArgumentException::new);
    }

    public long countMatch(List<Station> stationList) {
        if (this.stations.isEmpty()) {
            return -1;
        }

        final List<Station> validStations = validStations(stationList);

        return validStations.stream()
            .filter(stations::contains)
            .count();
    }

    public boolean contains(Station station) {
        return stations.contains(station);
    }
}
