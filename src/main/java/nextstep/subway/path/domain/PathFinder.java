package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.PathResponse;
import nextstep.subway.station.domain.Station;

public interface PathFinder {
    PathResponse getShortestPath(Lines lines, Station source, Station target);
}
