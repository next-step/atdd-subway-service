package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private Integer distance;
    private Integer fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, Integer distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public PathResponse(Path path) {
        this.stations = path.getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        this.distance = path.getDistance().getDistance();
        this.fare = path.getFare().getValue();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getFare() {
        return fare;
    }
}
