package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stationResponses;
    private int distance;

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stationResponses = stations;
        this.distance = distance;
    }

    public PathResponse(Path path) {
        this.stationResponses = StationResponse.generateStationResponses(path.getStations());
        this.distance = path.getDistance();
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public int getDistance() {
        return distance;
    }
}
