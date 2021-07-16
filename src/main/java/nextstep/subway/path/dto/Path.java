package nextstep.subway.path.dto;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Stations;

public class Path {

    private Stations stations;
    private Distance distance;
    private Fare fare;

    public Path(Stations stations, Distance distance, Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public Stations getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    public Fare getFare() {
        return fare;
    }
}
