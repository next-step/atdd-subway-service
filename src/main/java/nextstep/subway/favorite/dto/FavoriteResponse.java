package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Favorite favorite, StationResponse source, StationResponse target) {
        this.id = favorite.getId();
        this.source = source;
        this.target = target;
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
