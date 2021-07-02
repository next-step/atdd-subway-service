package nextstep.subway.path.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private double distance;
    private double fare;

    public PathResponse(List<StationResponse> stationList, double distance, double fare) {
        this.stations = stationList;
        this.distance = distance;
        this.fare = fare;
    }

    public double getDistance() {
        return this.distance;
    }

    public List<StationResponse> getStations() {
        return this.stations;
    }

    public double getFare() {
        return this.fare;
    }
}
