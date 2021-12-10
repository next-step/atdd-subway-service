package nextstep.subway.path.dto;

import java.util.*;

import nextstep.subway.path.domain.*;
import nextstep.subway.station.domain.*;

public class PathResponse {
    private final List<Station> stations;
    private final int totalDistance;
    private final int fare;

    private PathResponse(List<Station> stations, int totalDistance, int fare) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.fare = fare;
    }

    public static PathResponse from(Path path) {
        return new PathResponse(path.getStations(), path.getTotalDistance(), path.getFare());
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

    @Override
    public String toString() {
        return "Path{" +
            "stations=" + stations +
            ", totalDistance=" + totalDistance +
            '}';
    }
}
