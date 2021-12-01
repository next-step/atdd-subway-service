package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.List;

public class Stations {
    private final List<Station> stations;

    public Stations() {
        stations = new ArrayList<>();
    }

    public Stations(final List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

    public boolean isIncluded(Station station) {
        return stations.contains(station);
    }

    public boolean isIn(Station station) {
        return stations.contains(station);
    }
}
