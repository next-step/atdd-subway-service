package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;
    private Integer distance;
    private Integer pay;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, int pay) {
        this.stations = stations;
        this.distance = distance;
        this.pay = pay;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getPay() {
        return pay;
    }
}
