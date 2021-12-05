package nextstep.subway.path.dto;

import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName : nextstep.subway.path.dto
 * fileName : PathResponse
 * author : haedoang
 * date : 2021/12/04
 * description :
 */
@NoArgsConstructor
public class PathResponse {
    private List<StationResponse> stations;

    private PathResponse(List<StationResponse> stations) {
        this.stations = new ArrayList<>(stations);
    }

    public static PathResponse of(List<Station> stations) {
        List<StationResponse> stationResponses =
                stations.stream()
                        .map(StationResponse::of)
                        .collect(Collectors.toList());

        return new PathResponse(stationResponses);
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
