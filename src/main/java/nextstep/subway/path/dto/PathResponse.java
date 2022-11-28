package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;

    private int distance;

    public PathResponse() {
    }

    private PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(List<Station> stations, int distance) {
        return new PathResponse(
                stations.stream()
                        .map(it -> StationResponse.of(it.getId(), it.getName(), it.getCreatedDate()))
                        .collect(Collectors.toList()),
                distance);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
