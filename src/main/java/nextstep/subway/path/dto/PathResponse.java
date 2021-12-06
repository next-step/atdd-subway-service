package nextstep.subway.path.dto;

import java.util.*;

import nextstep.subway.path.domain.*;
import nextstep.subway.station.domain.*;

public class PathResponse {
    private final List<Station> stations;
    private final int totalDistance;

    private PathResponse(List<Station> stations, int totalDistance) {
        this.stations = stations;
        this.totalDistance = totalDistance;
    }

    public static PathResponse from(Path path) {
        return new PathResponse(path.getStations(), path.getTotalDistance());
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
