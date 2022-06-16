package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private Integer pathLength;
    private Integer pathSurcharge;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, Integer pathLength, Integer pathSurcharge) {
        this.stations = stations;
        this.pathLength = pathLength;
        this.pathSurcharge = pathSurcharge;
    }

    public PathResponse(Path path) {
        this.stations = path.getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        this.pathLength = path.getPathLength();
        this.pathSurcharge = path.getPathSurcharge();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getPathLength() {
        return pathLength;
    }

    public Integer getPathSurcharge() {
        return pathSurcharge;
    }
}
