package nextstep.subway.favorite.dto;

import lombok.Getter;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

@Getter
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    private FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        StationResponse source = StationResponse.of(favorite.getSource());
        StationResponse target = StationResponse.of(favorite.getTarget());
        return new FavoriteResponse(favorite.getId(), source, target);
    }
}
