package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;

    private StationResponse source;

    private StationResponse target;
    private FavoriteResponse(final Long id, final StationResponse source, final StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public FavoriteResponse() {

    }

    public static FavoriteResponse from(final Favorite favorite) {
        StationResponse sourceStation = StationResponse.of(favorite.getSource());
        StationResponse targetStation = StationResponse.of(favorite.getTarget());
        return new FavoriteResponse(favorite.getId(), sourceStation, targetStation);
    }

    public Long getId() {
        return this.id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
