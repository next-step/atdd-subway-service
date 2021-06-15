package nextstep.subway.line.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import java.util.List;

public interface ShortestDistance {
    Distance shortestDistance(List<Section> sections, Station source, Station target);
    Stations shortestRoute(List<Section> sections, Station source, Station target);
}
