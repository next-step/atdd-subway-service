package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;

    public PathResponse(Path path) {
        this.stations = toStationResponse(path);
        this.distance = path.findDistance().getDistance();
    }

    private static List<StationResponse> toStationResponse(Path path) {
        return path.findStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public List<StationResponse> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }
}
