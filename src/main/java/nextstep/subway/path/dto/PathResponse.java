package nextstep.subway.path.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;


public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    protected PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this(stations, distance, 0);
    }

    public PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }
}
