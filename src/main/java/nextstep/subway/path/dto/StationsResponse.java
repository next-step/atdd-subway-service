package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Stations;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class StationsResponse {
    private List<StationResponse> stations;

    public StationsResponse(List<StationResponse> stations) {
        this.stations = stations;
    }

    public static StationsResponse of(Stations stations) {
        return new StationsResponse(
                stations.toList()
                        .stream()
                        .map(StationResponse::of)
                        .collect(Collectors.toList())
        );
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
