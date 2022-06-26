package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

public class Path {
    private Station source;
    private Station target;

    public Path(Station source, Station target) {
        this.source = source;
        this.target = target;
    }

    public boolean isSame(Station upStation, Station downStation) {
        return (source.match(upStation) && target.match(downStation))
                || (source.match(downStation) && target.match(upStation));
    }
}
