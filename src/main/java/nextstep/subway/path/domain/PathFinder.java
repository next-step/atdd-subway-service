package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResult;
import nextstep.subway.station.domain.Station;

import java.util.Set;

public interface PathFinder {
    PathResult findShortCut(Set<Line> lines, Station source, Station target);
}
