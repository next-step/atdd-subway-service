package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final Distance distance;
    private final List<Station> stations;

    private Path(Distance distance, List<Station> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public static Path of(Distance distance, List<Station> stations) {
        return new Path(distance, stations);
    }

    public Distance getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }
}
