package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-06
 */
public class PathResponse {

    private final int distance;

    private final List<StationResponse> stations;

    private PathResponse(int distance, List<StationResponse> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public static PathResponse of(int distance, List<Station> stations) {
        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(distance, stationResponses);
    }

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
