package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

public interface PathFinderStrategy {
    Path getShortestDistance(Lines lines, Station source, Station target);
}
