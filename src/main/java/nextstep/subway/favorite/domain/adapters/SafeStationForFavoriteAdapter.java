package nextstep.subway.favorite.domain.adapters;

import nextstep.subway.exceptions.EntityNotFoundException;
import nextstep.subway.favorite.domain.excpetions.SafeStationInFavoriteException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.application.exceptions.StationEntityNotFoundException;
import nextstep.subway.station.domain.Station;
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

    @Override
    public SafeStationInFavorite getSafeStationInFavorite(Long stationId) {
        try {
            Station station = stationService.findStationById(stationId);
            return new SafeStationInFavorite(station);
        } catch (StationEntityNotFoundException e) {
            throw new EntityNotFoundException("역 정보를 불러오는 중 오류가 발생했습니다.");
        } catch (RuntimeException e) {
            throw new SafeStationInFavoriteException("Station 도메인에서 알 수 없는 오류가 발생했습니다.");
        }
    }
}
