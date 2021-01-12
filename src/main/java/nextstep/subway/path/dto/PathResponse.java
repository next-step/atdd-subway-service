package nextstep.subway.path.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int payment;

    public PathResponse(List<StationResponse> stations, int distance, int payment) {
        this.stations = stations;
        this.distance = distance;
        this.payment = payment;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getPayment() {
        return payment;
    }
}
