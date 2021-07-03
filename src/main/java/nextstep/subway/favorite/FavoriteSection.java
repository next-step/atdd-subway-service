package nextstep.subway.favorite;

import nextstep.subway.station.domain.Station;

public class FavoriteSection {
    private final int id;
    private final Station source;
    private final Station target;

    public FavoriteSection(final int id, final Station source, final Station target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public int getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
