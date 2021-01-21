package nextstep.subway.path.dto;

import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private long distance;
    private int fare;

    public static PathResponse from(ShortestPath shortestPath) {
        return new PathResponse(convertStationResponse(shortestPath.getStations()), shortestPath.getDistance(), shortestPath.getFare());
    }

    public PathResponse(List<StationResponse> stations, long distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public PathResponse() {
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public long getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }

    private static List<StationResponse> convertStationResponse(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
