package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Fare_bak;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;

    private int distance;

    private int fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare.getFare();
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
