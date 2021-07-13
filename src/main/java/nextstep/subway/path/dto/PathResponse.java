package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private long fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, long fare) {
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

    public long getFare() {
        return fare;
    }
}
