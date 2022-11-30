package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;

public class Path {
    private final List<Station> stations;
    private final long distance;

    public Path(List<Station> stations, double distance) {
        this.stations = stations;
        this.distance = (long) distance;
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public long getDistance() {
        return distance;
    }
}
