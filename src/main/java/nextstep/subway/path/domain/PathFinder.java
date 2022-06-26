package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

public interface PathFinder {
    ShortestPath findShortestPath(Station sourceStation, Station targetStation);
}
