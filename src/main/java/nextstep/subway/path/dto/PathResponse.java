package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;

    private PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse from(Path path) {
        return new PathResponse(
            path.getStations().getList().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList()),
            path.getDistanceValue()
        );
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
