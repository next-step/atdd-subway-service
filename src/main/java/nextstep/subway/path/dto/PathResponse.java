package nextstep.subway.path.dto;

import nextstep.subway.path.domain.PathResult;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class PathResponse {

    private List<StationResponse> stations;

    private int distance;

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public PathResponse(PathResult pathResult) {
        this.stations = pathResult.getStations().stream()
                .map(StationResponse::of)
                .collect(toList());
        this.distance = pathResult.getDistance().getValue();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
