package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;

public class FavoritePair {
    private Long id;
    private Station source;
    private Station target;

    public FavoritePair(Long id, Station source, Station target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoritePair of(Favorite favorite) {
        return new FavoritePair(favorite.getId(), favorite.getSource(), favorite.getTarget());
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
