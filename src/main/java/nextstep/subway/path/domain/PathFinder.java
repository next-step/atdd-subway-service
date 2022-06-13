package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

public interface PathFinder {
    Path findShortestPath(Station sourceStation, Station targetStation);
}
