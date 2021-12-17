package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class ShortestPath {

    private List<Station> stations;
    private Distance distance;
    private Fare fare;

    public ShortestPath(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = new Distance(distance);
        this.fare = Fare.overFareCalculate(this.distance);
    }

    public List<Station> findPaths() {
        return this.stations;
    }

    public Distance findDistance() {
        return this.distance;
    }

    public Fare findFare() {
        return this.fare;
    }
}
