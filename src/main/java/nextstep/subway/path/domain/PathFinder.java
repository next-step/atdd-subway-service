package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

public interface PathFinder {

    Path findPath(Lines lines, Station source, Station target);
}
