package nextstep.subway.path.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public int getDistance() {
        return this.distance;
    }

    public List<StationResponse> getStations() {
        return this.stations;
    }
}
