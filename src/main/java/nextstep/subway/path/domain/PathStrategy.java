package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

public interface PathStrategy {
    PathFinder getShortPath(Station source, Station target);
}
