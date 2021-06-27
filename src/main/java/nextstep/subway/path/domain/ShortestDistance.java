package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Stations;
import nextstep.subway.wrapped.Distance;

import java.util.List;

public interface ShortestDistance {
    Distance shortestDistance();
    Stations shortestRoute();
    List<Line> usedLines();
}
