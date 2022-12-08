package nextstep.subway.station.domain;

import java.util.List;
import java.util.stream.Collectors;

public class StationIds {
    private final List<StationId> stationIds;

    private StationIds(List<StationId> stationIds) {
        this.stationIds = stationIds;
    }

    public static StationIds from(List<StationId> stationIds) {
        return new StationIds(stationIds);
    }

    public List<Long> toLongList() {
        return stationIds.stream()
            .map(StationId::getLong)
            .collect(Collectors.toList());
    }
}
