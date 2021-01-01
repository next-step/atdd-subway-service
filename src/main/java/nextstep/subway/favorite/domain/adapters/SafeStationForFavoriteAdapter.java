package nextstep.subway.favorite.domain.adapters;

import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<SafeStationInFavorite> getSafeStationsInFavorite(List<Long> stationIds) {
        List<Station> stations = stationService.findAllStationsByIds(stationIds);

        return stations.stream()
                .map(SafeStationInFavorite::new)
                .collect(Collectors.toList());
    }
}
