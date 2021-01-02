package nextstep.subway.favorite.domain.adapters;

import java.util.List;

public interface SafeStationForFavorite {
    boolean isAllExists(Long source, Long target);
    SafeStationInFavorite getSafeStationInFavorite(Long stationId);
}
