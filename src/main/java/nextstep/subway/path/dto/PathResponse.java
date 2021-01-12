package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private long distance;

    public static PathResponse of(List<Station> stations, long distance) {
        return new PathResponse(convertStationResponse(stations), distance);
    }

    public PathResponse(List<StationResponse> stations, long distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public PathResponse() {
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public long getDistance() {
        return distance;
    }

    private static List<StationResponse> convertStationResponse(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
