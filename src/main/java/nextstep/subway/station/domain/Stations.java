package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.List;

public class Stations {
    private final List<Station> stations = new ArrayList<>();

    public void add(Station station) {
        stations.add(station);
    }

    public List<Station> getStations() {
        return stations;
    }
}
