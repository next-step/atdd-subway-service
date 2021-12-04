package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;

public class Path {
    List<Station> stations;

    private Path(List<Station> stations) {
        this.stations = stations;
    }

    public static Path of(List<Station> stations) {
        return new Path(stations);
    }

    public int getPathSize() {
        return stations.size();
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }
}
