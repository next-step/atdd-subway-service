package nextstep.subway.path.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.fares.domain.Fare;
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
    private int fare;

    public static PathResponse of(Path shortestPath, Fare fare) {
        List<StationResponse> stationResponses = shortestPath.getStations()
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        return new PathResponse(stationResponses, shortestPath.getDistance().getValue(), fare.getFare());
    }
}
