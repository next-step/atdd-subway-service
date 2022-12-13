package nextstep.subway.line.dto;

import java.util.List;
import nextstep.subway.line.domain.Charge;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int charge;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this(stations, distance, 0);
    }

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
