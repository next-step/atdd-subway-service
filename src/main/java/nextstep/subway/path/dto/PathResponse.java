package nextstep.subway.path.dto;

import java.util.List;

public class PathResponse {
    private final List<PathStation> stations;
    private final int distance;

    public PathResponse(List<PathStation> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(List<PathStation> stations, int distance) {
        return new PathResponse(stations, distance);
    }

    public List<PathStation> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
