package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    private PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse from(List<Station> stations, int distance) {
        return new PathResponse(stationsResponse(stations), distance);
    }

    private static List<StationResponse> stationsResponse(List<Station> stations) {
        return stations.stream()
                .map(eachStation -> StationResponse.of(eachStation))
                .collect(Collectors.toList());
    }

    public int getDistance() {
        return distance;
    }
}