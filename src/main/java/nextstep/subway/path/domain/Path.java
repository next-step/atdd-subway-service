package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.SurCharge;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {

    private List<Station> stations;
    private Distance distance;
    private SurCharge maxSurCharge;
    private Fare fare;

    public Path(List<Station> stations, Distance distance, SurCharge maxSurCharge) {
        this.stations = stations;
        this.distance = distance;
        this.maxSurCharge = maxSurCharge;
    }

    public Path calculatePathFare(int age) {
        fare = new Fare(distance, maxSurCharge, age);
        return this;
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

    public SurCharge getMaxSurCharge() {
        return maxSurCharge;
    }
}
