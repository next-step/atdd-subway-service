package nextstep.subway.favorite.domain.adapters;

import nextstep.subway.station.application.StationService;
import org.springframework.stereotype.Component;

@Component
public class SafeStationAdapter implements SafeStation {
    private final StationService stationService;

    public SafeStationAdapter(StationService stationService) {
        this.stationService = stationService;
    }

    @Override
    public boolean isExistStation(final Long stationId) {
        return stationService.isExistStation(stationId);
    }
}
