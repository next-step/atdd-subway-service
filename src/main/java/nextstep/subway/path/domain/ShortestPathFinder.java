package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

public interface ShortestPathFinder {
    static ShortestPathFinder getDefault(Lines lines) {
        return PathFinder.init(lines);
    }

    Path findShortestPath(Station sourceStation, Station targetStation);
}
