package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(Favorite favorite) {
        this.id = favorite.getId();
        this.source = StationResponse.of(favorite.getSource());
        this.target = StationResponse.of(favorite.getTarget());
    }

    public Long getId() {
        return this.id;
    }

    public StationResponse getSource() {
        return this.source;
    }

    public StationResponse getTarget() {
        return this.target;
    }
}
