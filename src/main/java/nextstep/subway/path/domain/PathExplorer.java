package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

public interface PathExplorer {
    Path explore(Station source, Station target);
}
