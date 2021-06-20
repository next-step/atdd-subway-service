package nextstep.subway.path.dto;

import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    private PathResponse(List<Station> stations, int distance) {
        this.stations = stations.stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());
        this.distance = distance;
    }

    public static PathResponse of(List<Station> stations, int distance) {
        return new PathResponse(stations, distance);
    }

    public static PathResponse of(ShortestPath shortestPath) {
        return new PathResponse(shortestPath.stations(), shortestPath.totalDistance());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
