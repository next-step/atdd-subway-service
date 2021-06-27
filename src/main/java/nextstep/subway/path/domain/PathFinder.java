package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.dto.PathStation;

import java.util.List;

public interface PathFinder {
    List<PathStation> findPath(PathStation source, PathStation target);

    Distance findShortestDistance(PathStation source, PathStation target);
}
