package nextstep.subway.path.dto;

import java.util.ArrayList;
import java.util.List;

import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations = new ArrayList<>();
    private int distance;
    private int fare;

    public PathResponse() {}

    private PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(List<StationResponse> stations, int distance, int fare) {
        return new PathResponse(stations, distance, fare);
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
