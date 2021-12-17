package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    public PathResponse(final List<StationResponse> stations, final int distance, final int fare) {
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
