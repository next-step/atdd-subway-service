package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int cost;

    public PathResponse(Path shortestPath) {
        this.stations = createStationResponses(shortestPath);
        this.distance = shortestPath.getDistance();
        this.cost = shortestPath.getCost();
    }

    private List<StationResponse> createStationResponses(Path shortestPath) {
        return shortestPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getCost() {
        return cost;
    }
}
