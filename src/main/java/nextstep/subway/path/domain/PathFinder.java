package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathFinder {
    Path findShortestPath(List<Section> sections, Station sourceStation, Station targetStation);
}
