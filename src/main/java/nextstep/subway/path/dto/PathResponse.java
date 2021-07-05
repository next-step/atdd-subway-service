package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(List<StationResponse> stationResponses, Distance distance, Fare fare) {
        return new PathResponse(stationResponses, distance.toInt(), fare.amount());
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
