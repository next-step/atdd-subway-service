package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathStrategy {
    Path getShortestPath(final List<Line> stations, final Station source, final Station target);
}
