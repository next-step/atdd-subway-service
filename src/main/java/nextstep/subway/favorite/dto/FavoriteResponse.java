package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {

    private final Long id;
    private final StationResponse source;
    private final StationResponse target;

    private FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favoriteResult) {
        StationResponse sourceStationResponse = StationResponse.of(favoriteResult.getSourceStation());
        StationResponse targetStationResponse = StationResponse.of(favoriteResult.getTargetStation());

        return new FavoriteResponse(favoriteResult.getId(), sourceStationResponse, targetStationResponse);
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
