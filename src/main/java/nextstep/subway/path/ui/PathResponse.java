package nextstep.subway.path.ui;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public PathResponse(final List<StationResponse> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    protected PathResponse() {}

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
