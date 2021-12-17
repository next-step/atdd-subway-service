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

    public static Path of(Lines lines, List<Station> stations, int distance) {
        return new Path(stations, distance, FareCalculator.calculator(lines, stations, distance));
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
}
