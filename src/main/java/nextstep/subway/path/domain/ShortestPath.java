package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class ShortestPath {
    private final List<Station> stations;
    private final int distance;
    private final int lineExtraFare;

    public ShortestPath(List<Station> stations, int distance, Lines lines) {
        this.stations = stations;
        this.distance = distance;
        this.lineExtraFare = lines.maxExtraFare();
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int calculateFare(int age) {
        Fare originFare = new Fare(lineExtraFare).add(FareDistance.calculate(distance));
        return originFare.subtract(AgeDiscount.calculate(age, originFare.value())).value();
    }
}
