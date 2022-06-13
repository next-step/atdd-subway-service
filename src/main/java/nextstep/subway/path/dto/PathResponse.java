package nextstep.subway.path.dto;

import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    private PathResponse() {}

    public PathResponse(ShortestPath shortestPath) {
        this.stations = mapToStationResponse(shortestPath.getPath());
        this.distance = shortestPath.getDistance();
    }

    private List<StationResponse> mapToStationResponse(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(toList());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
