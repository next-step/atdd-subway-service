package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;

    private PathResponse() {
    }

    public PathResponse(List<StationResponse> stations) {
        this.stations = stations;
    }

    public static PathResponse of(Path path) {
        List<StationResponse> stationsResponses = path.getStations().stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());
        return new PathResponse(stationsResponses);
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
