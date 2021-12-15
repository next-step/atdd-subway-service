package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {

    private static final int BASE_FARE = 1250;
    private List<Station> stations;
    private Distance distance;
    private Fare fare;

    private Path(List<Station> stations, Distance distance, Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static Path of(List<Station> stations, int distance) {
        return new Path(stations, Distance.from(distance), Fare.from(BASE_FARE));
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    public Fare getFare() {
        return fare;
    }
}
