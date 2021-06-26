package nextstep.subway.path.dto;

import static java.util.Collections.*;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;

    public static PathResponse of(Path path) {
        return new PathResponse(
            path.getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList()),
            path.getDistance());
    }

    public PathResponse(List<StationResponse> stations, double distance) {
        this.stations = stations;
        this.distance = (int)distance;
    }

    public List<StationResponse> getStations() {
        return unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }
}
