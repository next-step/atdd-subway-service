package nextstep.subway.path.domain;

import nextstep.subway.enums.ErrorMessage;
import nextstep.subway.line.domain.*;
import nextstep.subway.station.domain.Station;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Path {
    private final Distance distance;
    private final List<Station> stations;
    private final List<Line> lines;

    private Path(Distance distance, List<Station> stations, List<Line> lines) {
        this.distance = distance;
        this.stations = stations;
        this.lines = lines;
    }

    public static Path of(Distance distance, List<Station> stations, List<Line> lines) {
        return new Path(distance, stations, lines);
    }

    public Fare calculateFare(Integer age) {
        Fare fare = AddedFarePolicyByDistance.calculate(this.distance.value());
        if (Objects.nonNull(age)) {
            fare = DiscountPolicyByAge.calculate(fare.value(), age);
        }

        Fare maxAddedFare = this.lines.stream()
                .map(Line::getAddedFare)
                .max(Comparator.comparingInt(Fare::value))
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_FOUND.getMessage()));

        return fare.plus(maxAddedFare);
    }

    public Distance getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }
}
