package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.dto.StationResponse;

public class Path {
    private final List<StationResponse> stationResponses;
    private final int distance;

    public Path(List<StationResponse> stationResponses, int distance) {
        this.stationResponses = stationResponses;
        this.distance = distance;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public int getDistance() {
        return distance;
    }
}