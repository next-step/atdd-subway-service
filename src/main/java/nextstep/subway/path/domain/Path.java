package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Stations;
import nextstep.subway.station.domain.Station;

public class Path {
    private Stations stations;
    private Distance distance;

    private Fare fare;

    public Path(List<Station> stations, int distance, Fare fare) {
        this.stations = Stations.of(stations);
        this.distance = Distance.of(distance);
        this.fare = Fare.calculateFare(this.distance).add(fare);
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
