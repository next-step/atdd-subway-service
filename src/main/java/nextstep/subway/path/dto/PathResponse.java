package nextstep.subway.path.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    private PathResponse() {

    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse from(Path path) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        return new PathResponse(stations, path.getDistanceValue());
    }

    public List<StationResponse> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }
}
