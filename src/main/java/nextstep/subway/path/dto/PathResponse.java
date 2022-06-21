package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    List<StationResponse> stations;
    private int distance;

    private PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse from(Path shortestPath) {
        return new PathResponse(getStationResponses(shortestPath), shortestPath.getDistance());
    }

    private static List<StationResponse> getStationResponses(Path shortestPath) {
        return shortestPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
