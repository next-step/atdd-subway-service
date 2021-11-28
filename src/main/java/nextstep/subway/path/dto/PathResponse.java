package nextstep.subway.path.dto;

import java.util.List;

public class PathResponse {
    private List<PathStationDto> stations;
    private Integer distance;

    public PathResponse() {
    }

    public PathResponse(List<PathStationDto> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<PathStationDto> getStations() {
        return this.stations;
    }

    public Integer getDistance() {
        return this.distance;
    }
}
