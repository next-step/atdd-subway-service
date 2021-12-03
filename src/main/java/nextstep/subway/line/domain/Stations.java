package nextstep.subway.line.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

public final class Stations {
    private final List<Station> stations;

    private Stations(List<Station> stations) {
        this.stations = stations;
    }

    public static Stations from(List<Station> stations) {
        return new Stations(stations);
    }

    public boolean anyMatch(Station station) {
        return stations.stream().anyMatch(it -> it.equals(station));
    }

    public boolean noneMatch(Station station) {
        return stations.stream().noneMatch(it -> it.equals(station));
    }

    public boolean isEmpty() {
        return stations.isEmpty();
    }

    public List<Station> get() {
        return stations;
    }
}
