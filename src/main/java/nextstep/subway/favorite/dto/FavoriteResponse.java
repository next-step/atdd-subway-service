package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {

    private Long id;

    private StationResponse source;
    private StationResponse target;

    private FavoriteResponse() {}

    private FavoriteResponse(Long id) {
        this.id = id;
    }

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(favorite.getId());
    }

    public Long getId() {
        return this.id;
    }
    public StationResponse getSource() { return this.source; }
    public StationResponse getTarget() { return this.target; }
}
