package nextstep.subway.path.dto;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {
    private List<PathStationResponse> stations = new ArrayList<>();
    private int distance;
    private int fare;

    public PathResponse() {
    }

    public PathResponse(final List<PathStationResponse> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public PathResponse(List<PathStationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(final List<PathStationResponse> stations, final int distance) {
        return new PathResponse(stations, distance);
    }

    public static PathResponse of(final List<PathStationResponse> stations, final int distance, final int fare) {
        return new PathResponse(stations, distance, fare);
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
