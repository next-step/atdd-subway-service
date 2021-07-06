package nextstep.subway.path.dto;

import nextstep.subway.station.domain.StationsResponse;

public class PathResponse {
    private StationsResponse stations;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(StationsResponse stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public StationsResponse getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
