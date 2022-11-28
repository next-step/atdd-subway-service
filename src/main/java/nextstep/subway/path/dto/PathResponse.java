package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Fare;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse(List<StationResponse> stations, int distance, Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare.get();
    }

    private PathResponse() {}

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getFare() {
        return fare;
    }
}
