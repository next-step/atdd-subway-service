package nextstep.subway.favorite.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(final Long id, final StationResponse source, final StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public FavoriteResponse(final Favorite favorite) {
        this.id = favorite.getId();
        this.source = StationResponse.of(favorite.getSource());
        this.target = StationResponse.of(favorite.getTarget());
    }
}
