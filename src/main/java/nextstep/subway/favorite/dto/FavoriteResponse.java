package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public static FavoriteResponse of(Favorite favorite) {
        FavoriteResponse response = new FavoriteResponse();
        response.id = favorite.getId();
        response.source = StationResponse.of(favorite.getSource());
        response.target = StationResponse.of(favorite.getTarget());

        return response;
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
