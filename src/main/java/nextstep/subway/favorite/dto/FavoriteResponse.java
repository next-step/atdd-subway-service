package nextstep.subway.favorite.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

@Getter
@NoArgsConstructor
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(
            favorite.getId(),
            StationResponse.of(favorite.getSource()),
            StationResponse.of(favorite.getTarget())
        );
    }

    public FavoriteResponse(final Long id, final StationResponse source, final StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }
}
