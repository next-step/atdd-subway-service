package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return this.distance;
    }

}
