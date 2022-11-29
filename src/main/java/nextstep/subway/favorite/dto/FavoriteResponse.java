package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse sourceStation;
    private StationResponse targetStation;

    private FavoriteResponse() {}

    private FavoriteResponse(final Long id, final StationResponse sourceStation, final StationResponse targetStation) {
        this.id = id;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                StationResponse.of(favorite.getSource()),
                StationResponse.of(favorite.getTarget())
        );
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSourceStation() {
        return sourceStation;
    }

    public StationResponse getTargetStation() {
        return targetStation;
    }
}
