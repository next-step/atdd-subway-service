package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;

import nextstep.subway.station.domain.Station;

public class Path {
    private final List<Station> stations;
    private final int distance;

    public Path(final List<Station> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }
}
