package nextstep.subway.favorite.domain.adapters;

import nextstep.subway.station.application.StationService;
import org.springframework.stereotype.Component;

@Component
public class SafeStationForFavoriteAdapter implements SafeStationForFavorite {
    private final StationService stationService;

    public SafeStationForFavoriteAdapter(StationService stationService) {
        this.stationService = stationService;
    }

    @Override
    public boolean isAllExists(Long source, Long target) {
        return stationService.isExistStation(source) && stationService.isExistStation(target);
    }
}
