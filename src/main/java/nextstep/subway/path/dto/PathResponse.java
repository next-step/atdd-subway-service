package nextstep.subway.path.dto;

import java.util.List;

public class PathResponse {

    private final List<PathStationResponse> stations;
    private final int distance;
    private final int fee;

    public PathResponse(List<PathStationResponse> stations, int distance, int fee) {
        this.stations = stations;
        this.distance = distance;
        this.fee = fee;
    }

    public List<PathStationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }
}
