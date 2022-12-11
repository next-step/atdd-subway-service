package nextstep.subway.path.dto;

import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;

    private PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse from(PathFinder pathFinder) {
        List<StationResponse> response = pathFinder
                .getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new PathResponse(response, pathFinder.getDistance());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
