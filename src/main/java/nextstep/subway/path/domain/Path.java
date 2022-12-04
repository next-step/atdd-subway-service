package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;

public class Path {
    private final List<Station> stations;
    private final int distance;
    private int fare = 0;

    public Path(List<Station> stations, double distance) {
        this.stations = stations;
        this.distance = (int) distance;

        PathFare pathFare = PathFare.match(this.distance);
        this.fare = pathFare.getFare(this.distance);
    }

    public void calculateFare(int age, int lineFare) {
        fare = FareCalculator.calculate(age, fare, lineFare);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
