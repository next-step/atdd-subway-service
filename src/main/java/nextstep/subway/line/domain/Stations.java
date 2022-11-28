package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nextstep.subway.station.domain.Station;

public class Stations {
    private final List<Station> stations;

    private Stations(List<Station> stations) {
        this.stations = stations;
    }

    public static Stations of(List<Station> stations) {
        return new Stations(stations);
    }

    public static Stations empty() {
        return new Stations(new ArrayList<>());
    }

    public void add(Station station) {
        this.stations.add(station);
    }

    public List<Station> getList() {
        return Collections.unmodifiableList(this.stations);
    }
}
