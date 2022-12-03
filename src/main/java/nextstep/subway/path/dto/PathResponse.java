package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Path;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse from(Path path) {
        return new PathResponse(
                path.getStations()
                        .stream()
                        .map(it -> StationResponse.of(it.getId(), it.getName(), it.getCreatedDate()))
                        .collect(Collectors.toList()),
                path.getDistance());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

}
