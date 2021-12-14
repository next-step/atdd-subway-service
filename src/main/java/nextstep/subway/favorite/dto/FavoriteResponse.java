package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long Id;
    private StationResponse sourceStation;
    private StationResponse targetStation;

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), StationResponse.of(favorite.getSourceStation()), StationResponse.of(favorite.getTargetStation()));
    }

    public Long getId() {
        return Id;
    }

    public StationResponse getSourceStation() {
        return sourceStation;
    }

    public StationResponse getTargetStation() {
        return targetStation;
    }

    public FavoriteResponse(Long id, StationResponse sourceStation, StationResponse targetStation) {
        this.Id = id;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }
}
