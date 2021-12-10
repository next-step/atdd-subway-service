package nextstep.subway.path.dto;

import java.util.List;

public class PathResponse {

    private final List<PathStationResponse> stations;
    private final int distance;
    private final int fare;

    public PathResponse(List<PathStationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<PathStationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
