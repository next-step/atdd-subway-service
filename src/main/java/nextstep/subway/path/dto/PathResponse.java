package nextstep.subway.path.dto;

import java.util.List;

public class PathResponse {
    private List<PathStationDto> stations;
    private Integer distance;
    private Integer fare;

    public PathResponse() {
    }

    public PathResponse(List<PathStationDto> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
        this.fare = 0;
    }

    public PathResponse(List<PathStationDto> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<PathStationDto> getStations() {
        return this.stations;
    }

    public Integer getDistance() {
        return this.distance;
    }
    
    public Integer getFare() {
        return this.fare;
    }
}
