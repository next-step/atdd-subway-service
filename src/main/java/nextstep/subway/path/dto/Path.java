package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.path.domain.FareDistanceCalculator;
import nextstep.subway.station.domain.Station;

public class Path {

    private List<Station> stations;
    private int distance;
    private int extraFare;

    private Path(List<Station> stations, int distance, int extraFare) {
        this.stations = stations;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public static Path of(List<Station> stations, int distance, int extraFare) {
        return new Path(stations, distance, extraFare);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int calculateFare() {
        Fare fare = Fare.from(1250)
                .add(Fare.from(extraFare))
                .add(FareDistanceCalculator.calculate(distance));

        return fare.getValue();
    }
}
