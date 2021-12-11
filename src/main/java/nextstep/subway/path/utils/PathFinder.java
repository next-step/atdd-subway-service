package nextstep.subway.path.utils;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathFinder {
    PathResponse findPath(List<Line> line, Station sourceStation, Station targetStation);
}
