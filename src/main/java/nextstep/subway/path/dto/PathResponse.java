package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private double fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, double fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse from(Path path) {
        return new PathResponse(
                path.getStations().stream()
                        .map(StationResponse::of)
                        .collect(Collectors.toList()),
                path.getDistance().getValue(), path.getFare().getValue().doubleValue());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public double getFare() {
        return fare;
    }
}
