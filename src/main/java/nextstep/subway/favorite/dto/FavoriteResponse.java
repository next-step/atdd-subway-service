package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.FavoriteSection;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    private FavoriteResponse() {
    }

    public static FavoriteResponse of(FavoriteSection favoriteSection) {
        return new FavoriteResponse();
    }
}
