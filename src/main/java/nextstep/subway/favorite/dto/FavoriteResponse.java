package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;

public class FavoriteResponse {

    private Long id;
    private Station source;
    private Station target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Favorite favorite) {
        this.id = favorite.getId();
        this.source = favorite.getSource();
        this.target = favorite.getTarget();
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite);
    }

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
