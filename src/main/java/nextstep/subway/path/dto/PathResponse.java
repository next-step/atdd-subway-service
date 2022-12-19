package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public PathResponse() {
    }

    private PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse from(ShortestPath shortestPath) {
        return new PathResponse(StationResponse.of(shortestPath.getStations()), shortestPath.getDistance());
    }

    public static PathResponse from(List<Station> stations, int totalDistance) {
        return new PathResponse(StationResponse.of(stations), totalDistance);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
