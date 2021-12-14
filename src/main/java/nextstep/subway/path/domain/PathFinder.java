package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathFinder {

    Path findPath(List<Line> lines, Station sourceStation, Station targetStation);
}
