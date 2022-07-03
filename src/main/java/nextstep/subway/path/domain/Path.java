package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final List<Station> stations;
    private final int distance;

    private final int fare;

    public static Path of(List<Station> stations, int distance) {
        return new Path(stations, distance);
    }

    public Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
        this.fare = DistanceFarePolicy.BASIC_FARE + DistanceFarePolicy.calculateOverFare(distance);
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
