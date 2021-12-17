package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

public class Path {
    private List<Station> stations;
    private int distance;
    private int fare;

    private Path() {
    }

    private Path(List<Station> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static Path of(Lines lines, List<Station> stations, int distance, int age) {
        return new Path(stations, distance, FareCalculator.calculator(lines, stations, distance, age));
    }
    
    public static Path of(List<Station> stations, int distance) {
        return new Path(stations, distance, 0);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
    
    public void calculatorFare(Lines lines, int age) {
        this.fare = FareCalculator.calculator(lines, stations, distance, age);
    }
}
