package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class Path {
    private static final FareCalculator FARE_CALCULATOR = new FareCalculator();

    private List<Line> lines;
    private List<Station> stations;
    private Distance distance;

    public Path(List<Line> lines, List<Station> stations, Distance distance) {
        this.lines = lines;
        this.stations = stations;
        this.distance = distance;
    }

    public Fare calculateFare() {
        return Fare.from(FARE_CALCULATOR.calculate(distance, lines));
    }

    public Fare calculateFare(int age) {
        return Fare.from(FARE_CALCULATOR.calculate(distance, lines, age));
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }


    @Override
    public String toString() {
        return "Path{" + "stations=" + stations + ", distance=" + distance + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Path path = (Path) o;
        return Objects.equals(stations, path.stations) && Objects.equals(distance, path.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }
}
