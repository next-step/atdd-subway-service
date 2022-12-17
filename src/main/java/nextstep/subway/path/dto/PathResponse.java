package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<PathStationResponse> stations;

    private int distance;

    private PathResponse() {

    }

    public PathResponse(List<PathStationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(List<Station> stations, int distance) {
        List<PathStationResponse> stationResponses = stations.stream()
            .map(PathStationResponse::of)
            .collect(Collectors.toList());
        return new PathResponse(stationResponses, distance);
    }

    public List<PathStationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
