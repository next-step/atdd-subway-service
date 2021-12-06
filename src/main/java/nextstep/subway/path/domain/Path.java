package nextstep.subway.path.domain;

import java.util.*;

import nextstep.subway.station.domain.*;

public class Path {
    private final List<Station> stations;
    private final int totalDistance;

    private Path(List<Station> stations, int totalDistance) {
        this.stations = stations;
        this.totalDistance = totalDistance;
    }

    public static Path of(List<Station> stations, int totalDistance) {
        return new Path(stations, totalDistance);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    @Override
    public String toString() {
        return "Path{" +
            "stations=" + stations +
            ", totalDistance=" + totalDistance +
            '}';
    }
}
