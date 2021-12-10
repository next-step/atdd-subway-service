package nextstep.subway.path.dto;

import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int price;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, int price) {
        this.stations = stations;
        this.distance = distance;
        this.price = price;
    }

    public static PathResponse of(ShortestPath shortestPath, int price) {
        return new PathResponse(stationsToStationResponses(shortestPath.getStations()), shortestPath.getDistance(), price);
    }

    private static List<StationResponse> stationsToStationResponses(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getPrice() {
        return price;
    }
}
