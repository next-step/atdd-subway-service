package nextstep.subway.path.utils;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;

import java.util.List;

public interface PathFinder {
    PathResponse findPath(List<Line> line, Long sourceStation, Long targetStation);
}
