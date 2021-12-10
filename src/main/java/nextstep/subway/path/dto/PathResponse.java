package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PathResponse {
    private List<StationResponse> stationResponses = new ArrayList<>();
    private int distance;

    public PathResponse(List<StationResponse> stationResponses, int distance) {
        this.stationResponses = stationResponses;
        this.distance = distance;
    }

    public PathResponse() {}

    public List<StationResponse> getStations() {
        return stationResponses;
    }

    public int getDistance() {
        return distance;
    }
}
