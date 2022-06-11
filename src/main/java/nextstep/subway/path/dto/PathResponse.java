package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public PathResponse(Path path) {
       stations = path.getShortestPath().stream().
                map(station -> StationResponse.of(station)).
                collect(Collectors.toList());
        distance = path.getShortestDistance();
    }

    public List<StationResponse> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setStations(List<StationResponse> stations) {
        this.stations = stations;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
