package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    List<StationResponse> stations;
    Integer weight;

    public PathResponse(List<StationResponse> stations, Integer weight) {
        this.stations = stations;
        this.weight = weight;
    }

    public static PathResponse of(List<StationResponse> stations, Integer distance) {
        return new PathResponse(stations, distance);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getWeight() {
        return weight;
    }
}