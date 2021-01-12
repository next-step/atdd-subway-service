package nextstep.subway.favorite.dto;

import lombok.AllArgsConstructor;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

@AllArgsConstructor
public class FavoriteResponse {
    private final Long id;
    private final StationResponse source;
    private final StationResponse target;

    public Long getId() {
        return id;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                StationResponse.of(favorite.getSourceStation()),
                StationResponse.of(favorite.getTargetStation()));
    }
}
