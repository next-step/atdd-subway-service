package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> stations;
    private Distance distance;
    private int fare;

    protected PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = new Distance(distance);
        this.fare = fare;
    }

    public static PathResponse of(List<Station> stations, double distance, Fare fare) {
        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(stationResponses, (int) distance, fare.getValue());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
