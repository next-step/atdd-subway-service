package nextstep.subway.path.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    private PathResponse() {}

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
