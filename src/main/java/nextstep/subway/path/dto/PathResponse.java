package nextstep.subway.path.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {
    private static final int BASE_FARE = 1_250;
    private List<StationResponse> stations = new ArrayList<>();
    private int distance;
    private int fare = BASE_FARE;

    protected PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
