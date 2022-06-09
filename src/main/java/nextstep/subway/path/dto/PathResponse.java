package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    protected PathResponse() {
    }

    public PathResponse(Path path) {
        this.stations = StationResponse.of(path.getStations());
        this.distance = path.getDistance();
    }

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
