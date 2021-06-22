package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public PathResponse(final List<StationResponse> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse from(final Path shortestPath) {
        List<StationResponse> stationResponses = shortestPath.getStations()
                .stream()
                .map(StationResponse::of)
                .collect(toList());

        return new PathResponse(stationResponses, shortestPath.getDistance());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
