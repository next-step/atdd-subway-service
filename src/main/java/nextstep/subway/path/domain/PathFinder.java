package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

public interface PathFinder {
    ShortPath findShortPath(Station sourceStation, Station targetStation);
}
