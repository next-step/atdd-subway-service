package nextstep.subway.path.domain;

import java.util.*;

import nextstep.subway.station.domain.*;

public class Path {
    private final List<Station> stations;
    private final int totalDistance;
    private final int fare;

    private Path(List<Station> stations, int totalDistance, int fare) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.fare = fare;
    }

    public static Path of(List<Station> stations, int totalDistance) {
        return new Path(stations, totalDistance, 0);
    }

    public static Path of(List<Station> stations, int totalDistance, int fare) {
        return new Path(stations, totalDistance, fare);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getFare() {
        return fare;
    }
}
