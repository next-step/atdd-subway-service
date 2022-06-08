package nextstep.subway.station.dto;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.List;
import nextstep.subway.line.domain.Sections;

public class StationsResponse {
    List<StationResponse> stationsResponse;

    private StationsResponse(List<StationResponse> stationsResponse) {
        this.stationsResponse = stationsResponse;
    }

    public static StationsResponse of(Sections sections) {
        return sections.getStations().stream()
                .map(StationResponse::of)
                .collect(collectingAndThen(toList(), StationsResponse::new));
    }

    public List<StationResponse> getStations() {
        return stationsResponse;
    }
}
