package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;

    public PathResponse(final List<StationResponse> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(final Path path) {
        final List<StationResponse> stationResponses = path.getStations()
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        return new PathResponse(stationResponses, path.getDistance());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
