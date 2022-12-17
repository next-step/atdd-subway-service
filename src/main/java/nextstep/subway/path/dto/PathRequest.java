package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

public class PathRequest {
    private Station source;
    private Station target;

    private PathRequest() {
    }

    public PathRequest(Station source, Station target) {
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