package nextstep.subway.path.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    public static PathResponse of(Path shortestPath) {
        List<StationResponse> stationResponses = shortestPath.getStations()
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        return new PathResponse(stationResponses, shortestPath.getDistance());
    }
}
