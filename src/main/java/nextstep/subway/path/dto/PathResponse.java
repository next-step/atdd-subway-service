package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;

    private int distance;

    private int fare;

    public PathResponse() {
    }

    private PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }

    public static PathResponse of(List<Station> stations, int distance, int fare) {
        return new PathResponse(toStations(stations), distance, fare);
    }

    private static List<StationResponse> toStations(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }
}
