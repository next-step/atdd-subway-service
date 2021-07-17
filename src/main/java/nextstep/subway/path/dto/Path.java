package nextstep.subway.path.dto;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.domain.FareCalculator;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.member.domain.Age;
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

    public Path(Stations stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public void applyFare(Sections sections, Age age) {
        this.fare = FareCalculator.calculate(sections, age, this.stations, this.distance);
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
