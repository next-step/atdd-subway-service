package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations = new ArrayList<>();
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(List<Station> stations, int distance) {
        return new PathResponse(convertStationResponse(stations), distance);
    }

    private static List<StationResponse> convertStationResponse(List<Station> stations) {
        return stations.stream().map(StationResponse::of).collect(Collectors.toList());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
