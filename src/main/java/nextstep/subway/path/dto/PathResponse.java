package nextstep.subway.path.dto;

import nextstep.subway.member.dto.Money;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    private int distance;
    private List<StationResponse> stations;
    private long fee;

    public PathResponse(int distance, List<StationResponse> stations, long fee) {
        this.distance = distance;
        this.stations = stations;
        this.fee = fee;
    }

    public int getDistance() {
        return this.distance;
    }

    public List<StationResponse> getStations() {
        return this.stations;
    }

    public long getFee() {
        return fee;
    }

}
