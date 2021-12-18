package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public PathResponse() {

    }

    public PathResponse(List<StationResponse> stationResponses, int distance) {
        this.stations = stationResponses;
        this.distance = distance;
    }

    public static PathResponse of(Path path) {
        return new PathResponse(StationResponse.listOf(path.getStations()), path.sumPathDistance());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
