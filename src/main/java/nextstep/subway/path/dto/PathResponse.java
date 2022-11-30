package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations;

    private int distance;

    protected PathResponse() {}

    private PathResponse(Path path) {
        this.stations = path.getStations().stream()
            .map(StationResponse::from)
            .collect(Collectors.toList());
        this.distance = path.getDistance().value();
    }

    public static PathResponse from(Path path) {
        return new PathResponse(path);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
