package nextstep.subway.line.dto.path;

import java.util.List;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private final List<StationResponse> stations;
    private final Integer distance;

    public PathResponse(List<StationResponse> stations, Integer distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(List<StationResponse> stations, Integer distance) {
        return new PathResponse(stations, distance);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }
}
