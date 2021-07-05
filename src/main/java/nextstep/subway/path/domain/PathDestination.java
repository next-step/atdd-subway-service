package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

public class PathDestination {
    private Station source;
    private Station target;

    public PathDestination(Station source, Station target) {
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
