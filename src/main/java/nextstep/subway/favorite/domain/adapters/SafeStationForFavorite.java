package nextstep.subway.favorite.domain.adapters;

import java.util.List;

public interface SafeStationForFavorite {
    boolean isAllExists(Long source, Long target);
    List<SafeStationInFavorite> getSafeStationsInFavorite(List<Long> stationIds);
}
