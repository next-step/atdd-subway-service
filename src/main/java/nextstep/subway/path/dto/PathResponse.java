package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<PathStationResponse> stations;

    private int distance;

    private int fare;

    private PathResponse() {

    }

    public PathResponse(List<PathStationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(Path path) {
        return new PathResponse(
            toList(path.getStations()),
            path.getDistance(),
            path.getExtraFare()
        );
    }

    private static List<PathStationResponse> toList(List<Station> stations) {
        return stations.stream()
            .map(PathStationResponse::of)
            .collect(Collectors.toList());
    }

    public List<PathStationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
