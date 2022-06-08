package nextstep.subway.path.dto;

import java.util.Collections;
import java.util.List;
import nextstep.subway.station.dto.StationResponse;

public class ShortestPathResponse {
    private List<StationResponse> stations;
    public List<StationResponse> getStations() {
        return Collections.unmodifiableList(stations);
    }
}
