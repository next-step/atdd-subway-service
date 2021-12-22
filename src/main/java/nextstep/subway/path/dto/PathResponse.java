package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Fare;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int fare;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, Fare fare, int distance) {
        this.stations = stations;
        this.fare = fare.getFare();
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getFare() {
        return fare;
    }

    public int getDistance() {
        return distance;
    }
}
