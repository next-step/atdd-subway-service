package nextstep.subway.path.dto;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    private PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(ShortestPath shortestPath, Fare fare) {
        List<StationResponse> pathStations = shortestPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(pathStations, shortestPath.getDistance(), fare.getFare());
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
}
