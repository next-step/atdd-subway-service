package nextstep.subway.path.domain;

import nextstep.subway.station.domain.*;

public interface PathFinder {
    Path shortestPath(Station source, Station target);
}
