package nextstep.subway.path.application;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class LinePath {

    private final List<Station> stations;
    private final Distance distance;

    public LinePath(List<Station> stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static LinePath of(List<Station> stations, int distance) {
        return new LinePath(stations, new Distance(distance));
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }
}
