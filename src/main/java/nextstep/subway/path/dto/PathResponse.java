package nextstep.subway.path.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    public static PathResponse of(List<Station> stations, int distance) {
        List<StationResponse> stationResponses = stations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        return new PathResponse(stationResponses, distance);
    }
}
