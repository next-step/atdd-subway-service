package nextstep.subway.path.dto;

import static java.util.Collections.*;

import java.util.List;

import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    public List<StationResponse> getStations() {
        return unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }
}
