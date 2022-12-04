package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;

public class Path {
    private final List<Station> stations;
    private final Distance distance;
    private final Integer fare;

    public Path(List<Station> stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
        this.fare = calculateFare(distance);
    }

    private Integer calculateFare(Distance distance) {
        return FareCalculator.calculateFare(distance);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public Distance getDistance() {
        return distance;
    }
}
