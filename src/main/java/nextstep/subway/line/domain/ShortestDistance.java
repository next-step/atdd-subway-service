package nextstep.subway.line.domain;

import nextstep.subway.line.domain.wrapped.Distance;
import nextstep.subway.station.domain.Stations;

public interface ShortestDistance {
    Distance shortestDistance();
    Stations shortestRoute();
}
