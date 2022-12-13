package nextstep.subway.path.domain;

import nextstep.subway.line.domain.*;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Objects;

public class Path {
    private final Distance distance;
    private final List<Station> stations;
    private final Lines lines;

    private Path(Distance distance, List<Station> stations, Lines lines) {
        this.distance = distance;
        this.stations = stations;
        this.lines = lines;
    }

    public static Path of(Distance distance, List<Station> stations, Lines lines) {
        return new Path(distance, stations, lines);
    }

    public Fare calculateFare(Integer age) {
        Fare fare = AddedFarePolicyByDistance.calculate(this.distance.value());
        if (Objects.nonNull(age)) {
            fare = DiscountPolicyByAge.calculate(fare.value(), age);
        }
        return fare.plus(lines.getMaxAddedFare());
    }

    public Distance getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }
}
