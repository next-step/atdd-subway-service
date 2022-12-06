package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

public class FavoriteResponse {

    private Long id;
    private FavoriteStationResponse source;
    private FavoriteStationResponse target;

    public FavoriteResponse() {}

    public FavoriteResponse(Favorite favorite) {
        this.id = favorite.getId();
        this.source = FavoriteStationResponse.of(favorite.getSourceStation());
        this.target = FavoriteStationResponse.of(favorite.getTargetStation());
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite);
    }

    public Long getId() {
        return id;
    }

    public FavoriteStationResponse getSource() {
        return source;
    }

    public FavoriteStationResponse getTarget() {
        return target;
    }

}
