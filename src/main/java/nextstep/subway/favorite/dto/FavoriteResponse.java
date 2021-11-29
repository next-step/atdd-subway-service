package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;

public class FavoriteResponse {

    private final Long id;
    private final Station source;
    private final Station target;

    public FavoriteResponse(Long id, Station source, Station target) {
        this.id = id;
        this.source = source;
        this.target = target;
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
