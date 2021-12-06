package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private int distance;
    private List<StationResponse> stations;

    public PathResponse() {
    }

    private PathResponse(int distance, List<StationResponse> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public static PathResponse of(int distance, List<Station> stations) {
        return new PathResponse(distance, StationResponse.ofList(stations));
    }

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
