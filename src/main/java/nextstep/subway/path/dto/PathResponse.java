package nextstep.subway.path.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;
    private final int charge;

    public PathResponse(List<StationResponse> stations, int distance, int charge) {
        this.stations = stations;
        this.distance = distance;
        this.charge = charge;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getCharge() {
        return charge;
    }
}
