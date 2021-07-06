package nextstep.subway.station.domain;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class StationsResponse {

    private List<StationResponse> stations;

    public StationsResponse() {
    }

    public StationsResponse(List<StationResponse> stations) {
        this.stations = stations;
    }

    public static StationsResponse of(List<Station> stations) {
        List<StationResponse> lineResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new StationsResponse(lineResponses);
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
