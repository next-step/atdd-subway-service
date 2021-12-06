package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {

    private final Long id;
    private final StationResponse source;
    private final StationResponse target;

    private FavoriteResponse(Favorite favorite) {
        id = favorite.getId();
        source = StationResponse.of(favorite.getSource());
        target = StationResponse.of(favorite.getTarget());
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite);
    }

    public Long getId() {
        return id;
    }
}
