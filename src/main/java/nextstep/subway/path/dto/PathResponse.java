package nextstep.subway.path.dto;

import java.util.List;

public class PathResponse {

    private List<PathStationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse() {
    }

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
