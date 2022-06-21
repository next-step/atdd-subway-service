package nextstep.subway.path.dto;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {
    private List<PathStationResponse> stations = new ArrayList<>();

    private int distance;

    public PathResponse() {
    }

    public PathResponse(final List<PathStationResponse> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(final List<PathStationResponse> stations, final int distance) {
        return new PathResponse(stations, distance);
    }

    public List<PathStationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
