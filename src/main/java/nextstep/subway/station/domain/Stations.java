package nextstep.subway.station.domain;

import java.util.Collections;
import java.util.List;

public class Stations {
    private List<Station> stations;

    private Stations() {}

    private Stations(List<Station> stations) {
        this.stations = stations;
    }

    public static Stations from(List<Station> stations) {
        return new Stations(stations);
    }

    public boolean isLessThan(int size) {
        return stations.size() < size;
    }

    public List<Station> list() {
        return Collections.unmodifiableList(stations);
    }
}
