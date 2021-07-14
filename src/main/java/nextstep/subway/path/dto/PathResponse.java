package nextstep.subway.path.dto;

import nextstep.subway.station.domain.StationsResponse;

public class PathResponse {
    private StationsResponse stations;
    private int distance;
    private int fare;

    public PathResponse() {
    }

    public PathResponse(StationsResponse stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public PathResponse(StationsResponse stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public StationsResponse getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
