package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public PathResponse(){}

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(List<Station> stations, int distance) {
        return new PathResponse(getStations(stations), distance);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    private static List<StationResponse> getStations(List<Station> station) {
        return station.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
