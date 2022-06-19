package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.path.domain.Fee;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fee;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, Fee fee) {
        this.stations = stations;
        this.distance = distance;
        this.fee = fee.getFee();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }
}
