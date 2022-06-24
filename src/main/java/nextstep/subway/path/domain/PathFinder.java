package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathFinder {
    StationPath getShortestPath(final List<Line> lines, final Station start, final Station destination);
}
