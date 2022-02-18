package nextstep.subway.favorite.domain;

import nextstep.subway.station.domain.Station;

public class Favorite {

    private Long id;
    private Station source;
    private Station target;

    public Favorite(Long id, Station source, Station target) {
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
