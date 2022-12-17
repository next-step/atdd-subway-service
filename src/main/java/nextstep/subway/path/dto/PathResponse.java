package nextstep.subway.path.dto;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations = new ArrayList<>();

    public void add(StationResponse station) {
        stations.add(station);
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
