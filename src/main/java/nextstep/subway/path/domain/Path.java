package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Surcharge;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final List<Station> stations;
    private final Distance distance;
    private final Fare fare;

    public Path(List<Station> stations, Integer surcharge, Integer distance) {
        this.stations = stations;
        this.distance = new Distance(distance);
        this.fare = new Fare(new Surcharge(surcharge), new Distance(distance));
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
