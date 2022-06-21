package nextstep.subway.path.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations = Collections.emptyList();
    private long distance;
    private long fare;

    public PathResponse() {
    }

    private PathResponse(ShortestPath shortestPath, Fare fare) {
        this.stations = shortestPath.getStations().stream().map(StationResponse::of).collect(Collectors.toList());
        this.distance = shortestPath.getDistance();
        this.fare = fare.getWon();
    }

    public static PathResponse of(ShortestPath shortestPath, Fare fare) {
        return new PathResponse(shortestPath, fare);
    }

    public long getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public long getFare() {
        return fare;
    }
}
