package nextstep.subway.station.dto;

import nextstep.subway.station.domain.Stations;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StationResponseList {

    private final List<StationResponse> value;

    private StationResponseList(Stations stations) {
        this.value = stations.getValues().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public static StationResponseList of(Stations stations) {
        return new StationResponseList(stations);
    }

    public List<StationResponse> getValue() {
        return Collections.unmodifiableList(value);
    }
}
