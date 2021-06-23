package nextstep.subway.path.domain;

import nextstep.subway.wrapped.Distance;
import nextstep.subway.station.domain.Stations;

public interface ShortestDistance {
    Distance shortestDistance();
    Stations shortestRoute();
}
