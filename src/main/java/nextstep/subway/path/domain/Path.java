package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

public class Path {

    private final List<Station> stations;

    private final Distance distance;

    private Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = Distance.from(distance);
    }

    public static Path of(List<Station> stations, int distance) {
        return new Path(stations, distance);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public Distance getDistance() {
        return distance;
    }
}
