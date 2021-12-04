package nextstep.subway.path.dto;

import java.util.List;

public class PathResponse {

    private final List<PathStationResponse> stations;
    private final int distance;

    public PathResponse(List<PathStationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<PathStationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
