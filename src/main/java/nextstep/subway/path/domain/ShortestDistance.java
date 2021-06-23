package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Stations;
import nextstep.subway.wrapped.Distance;

public interface ShortestDistance {
    Distance shortestDistance();
    Stations shortestRoute();
    Lines usedLines();
}
