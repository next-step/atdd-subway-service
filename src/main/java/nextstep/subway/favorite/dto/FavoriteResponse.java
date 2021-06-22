package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;

public class FavoriteResponse {

    private Long id;
    private Station source;
    private Station target;

    public FavoriteResponse(Long id, Station sourceStation, Station targetStation) {
        this.id = id;
        this.source = sourceStation;
        this.target = targetStation;
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

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getSource(), favorite.getTarget());
    }
}
