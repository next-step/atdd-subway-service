package nextstep.subway.station.dto;

import java.util.List;

public class StationsResponse {

    private List<StationResponse> stations;

    public StationsResponse() {
    }

    public StationsResponse(List<StationResponse> stations) {
        this.stations = stations;
    }
}
