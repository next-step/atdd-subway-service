package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import java.util.List;

public interface ShortestDistance {
    Distance shortestDistance();
    Stations shortestRoute();
}
