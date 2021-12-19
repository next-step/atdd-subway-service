package nextstep.subway.path.domain;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<StationResponse> stationResponses = new ArrayList<>();
    private int distance;

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
