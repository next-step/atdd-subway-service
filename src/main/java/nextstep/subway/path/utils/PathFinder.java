package nextstep.subway.path.utils;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;

import java.util.List;

public interface PathFinder {
    Path findPath(List<Line> line, Long sourceStation, Long targetStation);
}
