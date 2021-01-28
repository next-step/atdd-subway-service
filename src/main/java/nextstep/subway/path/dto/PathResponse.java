package nextstep.subway.path.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    private int distance;
    private List<StationResponse> stations;

    public PathResponse(int distance, List<StationResponse> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public int getDistance() {
        return this.distance;
    }

    public List<StationResponse> getStations() {
        return this.stations;
    }

}
