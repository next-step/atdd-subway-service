package nextstep.subway.path.dto;

import nextstep.subway.path.application.LinePath;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private final List<StationResponse> stations;

    private final int distance;

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(LinePath linePath) {
        List<StationResponse> stationResponses = linePath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(stationResponses, linePath.getDistance().value());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }
}
