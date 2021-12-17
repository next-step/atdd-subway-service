package nextstep.subway.favorite.domain;

import nextstep.subway.station.domain.Station;

public class FavoriteRequest {
    private Station source;
    private Station target;

    public FavoriteRequest(Station source, Station target) {
        this.source = source;
        this.target = target;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
