package nextstep.subway.line.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.dto.StationResponse;

public final class StationResponses {
    private final List<StationResponse> stationResponses;

    private StationResponses(List<StationResponse> stationResponses) {
        this.stationResponses = stationResponses;
    }

    public static StationResponses from(Line line) {
        return new StationResponses(
            line.getStations().stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList()));
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }
}
