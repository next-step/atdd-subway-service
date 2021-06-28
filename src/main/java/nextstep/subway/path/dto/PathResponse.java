package nextstep.subway.path.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private double distance;

    public PathResponse(List<StationResponse> stationList, double distance) {
        this.stations = stationList;
        this.distance = distance;
    }

    public double getDistance() {
        return this.distance;
    }

    public List<StationResponse> getStations() {
        return this.stations;
    }
}
